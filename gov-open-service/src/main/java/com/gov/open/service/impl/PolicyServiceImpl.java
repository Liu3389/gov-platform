package com.gov.open.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gov.common.exception.BusinessException;
import com.gov.common.result.PageResult;
import com.gov.open.dto.PolicyDTO;
import com.gov.open.entity.PolicyEntity;
import com.gov.open.mapper.PolicyMapper;
import com.gov.open.service.PolicyService;
import com.gov.open.vo.PolicyVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 政策法规Service实现
 */
@Service
public class PolicyServiceImpl extends ServiceImpl<PolicyMapper, PolicyEntity> implements PolicyService {

    @Override
    public PageResult<PolicyVO> pageQueryVO(Long pageNum, Long pageSize, Integer policyType, Integer status) {
        LambdaQueryWrapper<PolicyEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(policyType != null, PolicyEntity::getPolicyType, String.valueOf(policyType));
        wrapper.eq(status != null, PolicyEntity::getStatus, String.valueOf(status));
        wrapper.orderByDesc(PolicyEntity::getCreateTime);

        Page<PolicyEntity> page = this.page(new Page<>(pageNum, pageSize), wrapper);
        List<PolicyVO> voList = page.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        return PageResult.of(voList, page.getTotal(), page.getCurrent(), page.getSize());
    }

    @Override
    public PolicyVO getVOById(Long id) {
        PolicyEntity entity = this.getById(id);
        if (entity == null || entity.getDeleted() == 1) {
            return null;
        }
        return convertToVO(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addPolicy(PolicyDTO dto, Long publishDeptId) {
        PolicyEntity entity = new PolicyEntity();
        BeanUtil.copyProperties(dto, entity);

        entity.setPolicyCode(generatePolicyCode());
        entity.setPublishDeptId(publishDeptId);
        entity.setPublishDeptName("测试部门"); // 默认部门名称
        entity.setViewCount(0);
        if (entity.getStatus() == null) {
            entity.setStatus("0"); // 默认草稿
        }
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());

        this.save(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePolicy(PolicyDTO dto, Long id) {
        PolicyEntity entity = this.getById(id);
        if (entity == null || entity.getDeleted() == 1) {
            throw new BusinessException(404, "政策法规不存在");
        }

        BeanUtil.copyProperties(dto, entity, "id", "policyCode", "publishDeptId", "viewCount", "createTime", "createBy");
        this.updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void publishPolicy(Long id) {
        PolicyEntity entity = this.getById(id);
        if (entity == null || entity.getDeleted() == 1) {
            throw new BusinessException(404, "政策法规不存在");
        }

        entity.setStatus("1"); // 已发布
        this.updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void abolishPolicy(Long id) {
        PolicyEntity entity = this.getById(id);
        if (entity == null || entity.getDeleted() == 1) {
            throw new BusinessException(404, "政策法规不存在");
        }

        entity.setStatus("2"); // 已废止
        this.updateById(entity);
    }

    /**
     * 生成政策编码
     */
    private String generatePolicyCode() {
        String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        return "POL" + dateStr;
    }

    /**
     * Entity转VO
     */
    private PolicyVO convertToVO(PolicyEntity entity) {
        PolicyVO vo = new PolicyVO();
        BeanUtil.copyProperties(entity, vo);
        vo.setPolicyTypeDesc(getPolicyTypeDesc(entity.getPolicyType()));
        vo.setStatusDesc(getStatusDesc(entity.getStatus()));
        return vo;
    }

    private String getPolicyTypeDesc(String type) {
        if (type == null) return "未知";
        switch (type) {
            case "1": return "法律法规";
            case "2": return "部门规章";
            case "3": return "规范性文件";
            case "4": return "政策解读";
            default: return "未知";
        }
    }

    private String getStatusDesc(String status) {
        if (status == null) return "未知";
        switch (status) {
            case "0": return "草稿";
            case "1": return "已发布";
            case "2": return "已废止";
            default: return "未知";
        }
    }
}
