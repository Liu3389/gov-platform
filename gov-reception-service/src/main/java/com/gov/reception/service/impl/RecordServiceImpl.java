package com.gov.reception.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gov.common.exception.BusinessException;
import com.gov.common.feign.UserFeignClient;
import com.gov.common.result.PageResult;
import com.gov.common.result.Result;
import com.gov.common.vo.ItemVO;
import com.gov.common.vo.UserVO;
import com.gov.common.vo.WorkflowTaskVO;
import com.gov.reception.dto.RecordAcceptDTO;
import com.gov.reception.dto.RecordQueryDTO;
import com.gov.reception.dto.RecordSubmitDTO;
import com.gov.reception.dto.StartProcessDTO;
import com.gov.reception.entity.*;
import com.gov.reception.feign.ActivitiFeignClient;
import com.gov.reception.feign.ItemFeignClient;
import com.gov.reception.mapper.RecordMapper;
import com.gov.reception.mapper.ReceptionLogMapper;
import com.gov.reception.service.MaterialService;
import com.gov.reception.service.RecordService;
import com.gov.reception.vo.MaterialVO;
import com.gov.reception.vo.RecordProgressVO;
import com.gov.reception.vo.RecordVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 办件Service实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RecordServiceImpl extends ServiceImpl<RecordMapper, RecordEntity> implements RecordService {

    private final MaterialService materialService;
    private final ReceptionLogMapper receptionLogMapper;
    private final ItemFeignClient itemFeignClient;
    private final ActivitiFeignClient activitiFeignClient;
    private final UserFeignClient userFeignClient;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RecordVO submitRecord(RecordSubmitDTO dto, Long userId) {
        // 1. 通过 Feign 查询事项信息（捕获所有异常，Feign故障不阻塞提交）
        ItemVO itemVO = null;
        try {
            Result<ItemVO> itemResult = itemFeignClient.getById(dto.getItemId());
            itemVO = (itemResult != null) ? itemResult.getData() : null;
        } catch (Exception e) {
            log.error("[提交办件] 查询事项信息失败 itemId={}", dto.getItemId(), e);
        }
        if (itemVO == null) {
            throw new BusinessException(404, "事项不存在");
        }

        // 2. 生成办件号
        String applyNo = generateApplyNo(dto.getDeptId());

        // 3. 创建办件记录
        RecordEntity entity = new RecordEntity();
        entity.setApplyNo(applyNo);
        entity.setItemId(dto.getItemId());
        entity.setUserId(userId);
        entity.setDeptId(itemVO.getDeptId() != null ? itemVO.getDeptId() : dto.getDeptId());
        entity.setStatus("0"); // 待受理
        entity.setRemark(dto.getRemark());
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        this.save(entity);

        // 4. 保存申报材料
        if (dto.getMaterials() != null && !dto.getMaterials().isEmpty()) {
            List<MaterialEntity> materials = dto.getMaterials().stream().map(m -> {
                MaterialEntity material = new MaterialEntity();
                material.setRecordId(entity.getId());
                material.setMaterialName(m.getMaterialName());
                material.setMaterialType(m.getMaterialType());
                material.setFileUrl(m.getFileUrl());
                material.setFileSize(m.getFileSize());
                material.setFileType(m.getFileType());
                material.setIsRequired(m.getIsRequired());
                material.setVerifyStatus(0);
                material.setCreateTime(LocalDateTime.now());
                material.setUpdateTime(LocalDateTime.now());
                return material;
            }).collect(Collectors.toList());
            materialService.saveBatch(materials);
        }

        // 5. 记录日志
        saveLog(entity.getId(), 1, "提交办件", userId, null);

        return getVOById(entity.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void acceptRecord(RecordAcceptDTO dto, Long operatorId, String operatorName) {
        RecordEntity entity = this.getById(dto.getRecordId());
        if (entity == null || entity.getDeleted() == 1) {
            throw new BusinessException(404, "办件不存在");
        }
        if (!"0".equals(entity.getStatus())) {
            throw new BusinessException(400, "该办件不在待受理状态");
        }

        if (dto.getAcceptResult() == 1) {
            // 受理通过
            entity.setStatus("1");
            entity.setWindowId(dto.getWindowId());
            entity.setOperatorId(operatorId);
            entity.setAcceptTime(LocalDateTime.now());
            saveLog(entity.getId(), 2, "受理通过", operatorId, operatorName);

            // ★ 核心：受理通过后启动审批流程
            try {
                // 获取事项信息
                String itemName = null;
                Long deptId = entity.getDeptId();
                try {
                    Result<ItemVO> itemResult = itemFeignClient.getById(entity.getItemId());
                    if (itemResult != null && itemResult.getData() != null) {
                        itemName = itemResult.getData().getItemName();
                        if (itemResult.getData().getDeptId() != null) {
                            deptId = itemResult.getData().getDeptId();
                        }
                    }
                } catch (Exception e) {
                    log.warn("[受理] 查询事项信息失败，使用默认值 itemId={}", entity.getItemId(), e);
                    itemName = "事项" + entity.getItemId();
                }

                // 获取用户姓名
                String userName = null;
                try {
                    Result<UserVO> userResult = userFeignClient.getById(entity.getUserId());
                    if (userResult != null && userResult.getData() != null) {
                        userName = userResult.getData().getRealName();
                        if (userName == null) {
                            userName = userResult.getData().getUsername();
                        }
                    }
                } catch (Exception e) {
                    log.warn("[受理] 查询用户信息失败，使用默认值 userId={}", entity.getUserId(), e);
                    userName = "用户" + entity.getUserId();
                }

                // 构建启动流程参数
                StartProcessDTO startProcessDTO = new StartProcessDTO();
                startProcessDTO.setProcessKey("apply_approval_v1");
                startProcessDTO.setApplyNo(entity.getApplyNo());
                startProcessDTO.setUserId(entity.getUserId());
                startProcessDTO.setDeptId(deptId);
                startProcessDTO.setItemId(entity.getItemId());
                startProcessDTO.setItemName(itemName);
                startProcessDTO.setUserName(userName);

                // 调用 Activiti 启动流程
                Result<WorkflowTaskVO> processResult = activitiFeignClient.startProcess(startProcessDTO);
                if (processResult != null && processResult.getData() != null) {
                    String processInstanceId = processResult.getData().getInstanceId();
                    entity.setProcessInstanceId(processInstanceId);
                    log.info("[受理] 审批流程启动成功 applyNo={} processInstanceId={}", entity.getApplyNo(), processInstanceId);
                } else {
                    log.warn("[受理] 审批流程启动返回空结果 applyNo={}", entity.getApplyNo());
                }
            } catch (Exception e) {
                // Feign 调用失败不阻塞受理（fallback 已降级）
                log.error("[受理] 启动审批流程失败，但不阻塞受理 applyNo={}", entity.getApplyNo(), e);
                saveLog(entity.getId(), 0, "流程启动失败：" + e.getMessage(), operatorId, operatorName);
            }
        } else {
            // 受理驳回
            entity.setStatus("4");
            entity.setRejectReason(dto.getRejectReason());
            saveLog(entity.getId(), 4, "受理驳回：" + dto.getRejectReason(), operatorId, operatorName);
        }

        this.updateById(entity);
    }

    @Override
    public PageResult<RecordVO> pageQueryVO(Long pageNum, Long pageSize, RecordQueryDTO queryDTO) {
        LambdaQueryWrapper<RecordEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RecordEntity::getDeleted, 0);
        wrapper.like(queryDTO.getApplyNo() != null, RecordEntity::getApplyNo, queryDTO.getApplyNo());
        wrapper.eq(queryDTO.getItemId() != null, RecordEntity::getItemId, queryDTO.getItemId());
        wrapper.eq(queryDTO.getUserId() != null, RecordEntity::getUserId, queryDTO.getUserId());
        wrapper.eq(queryDTO.getDeptId() != null, RecordEntity::getDeptId, queryDTO.getDeptId());
        wrapper.eq(queryDTO.getStatus() != null, RecordEntity::getStatus, queryDTO.getStatus());
        wrapper.orderByDesc(RecordEntity::getCreateTime);

        Page<RecordEntity> page = this.page(new Page<>(pageNum, pageSize), wrapper);
        List<RecordVO> voList = page.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        return PageResult.of(voList, page.getTotal(), page.getCurrent(), page.getSize());
    }

    @Override
    public RecordVO getVOById(Long id) {
        RecordEntity entity = this.getById(id);
        if (entity == null || entity.getDeleted() == 1) {
            return null;
        }
        RecordVO vo = convertToVO(entity);

        // 查询材料列表
        List<MaterialVO> materials = materialService.listByRecordId(id);
        vo.setMaterials(materials);

        return vo;
    }

    @Override
    public RecordProgressVO getRecordProgress(Long id) {
        RecordEntity entity = this.getById(id);
        if (entity == null || entity.getDeleted() == 1) {
            return null;
        }

        RecordProgressVO vo = new RecordProgressVO();
        vo.setId(entity.getId());
        vo.setApplyNo(entity.getApplyNo());
        vo.setStatus(entity.getStatus());
        vo.setStatusDesc(getStatusDesc(entity.getStatus()));
        vo.setCreateTime(entity.getCreateTime());
        vo.setAcceptTime(entity.getAcceptTime());
        vo.setFinishTime(entity.getFinishTime());

        // 查询进度日志
        LambdaQueryWrapper<ReceptionLogEntity> logWrapper = new LambdaQueryWrapper<>();
        logWrapper.eq(ReceptionLogEntity::getDeleted, 0);
        logWrapper.eq(ReceptionLogEntity::getRecordId, id);
        logWrapper.orderByAsc(ReceptionLogEntity::getActionTime);
        List<ReceptionLogEntity> logs = receptionLogMapper.selectList(logWrapper);

        List<RecordProgressVO.ProgressLogVO> logVOs = logs.stream().map(log -> {
            RecordProgressVO.ProgressLogVO logVO = new RecordProgressVO.ProgressLogVO();
            logVO.setActionType(Integer.parseInt(log.getActionType()));
            logVO.setActionDesc(log.getRemark());
            logVO.setOperatorName(log.getOperatorName());
            logVO.setOperateTime(log.getActionTime());
            logVO.setRemark(log.getRemark());
            return logVO;
        }).collect(Collectors.toList());
        vo.setLogs(logVOs);

        return vo;
    }

    @Override
    public String generateApplyNo(Long deptId) {
        // 办件号格式：年份 + 部门编码 + 序号
        String year = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy"));
        String deptCode = String.format("%02d", deptId % 100);

        // 使用时间戳作为序号
        String timestamp = String.valueOf(System.currentTimeMillis() % 1000000);
        return year + deptCode + String.format("%06d", Long.parseLong(timestamp));
    }

    /**
     * 保存办件日志
     */
    private void saveLog(Long recordId, Integer actionType, String actionDesc, Long operatorId, String operatorName) {
        ReceptionLogEntity logEntity = new ReceptionLogEntity();
        logEntity.setRecordId(recordId);
        logEntity.setActionType(String.valueOf(actionType));
        logEntity.setActionTime(LocalDateTime.now());
        logEntity.setOperatorId(operatorId);
        logEntity.setOperatorName(operatorName);
        logEntity.setRemark(actionDesc);
        logEntity.setCreateTime(LocalDateTime.now());
        logEntity.setUpdateTime(LocalDateTime.now());
        receptionLogMapper.insert(logEntity);
    }

    /**
     * Entity转VO
     */
    private RecordVO convertToVO(RecordEntity entity) {
        RecordVO vo = new RecordVO();
        BeanUtil.copyProperties(entity, vo);
        vo.setStatusDesc(getStatusDesc(entity.getStatus()));
        return vo;
    }

    /**
     * 获取状态描述
     */
    private String getStatusDesc(String status) {
        if (status == null) return "未知";
        switch (status) {
            case "0": return "待受理";
            case "1": return "受理中";
            case "2": return "审批中";
            case "3": return "办结";
            case "4": return "驳回";
            default: return "未知";
        }
    }
}
