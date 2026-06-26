package com.gov.reception.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gov.common.exception.BusinessException;
import com.gov.common.result.PageResult;
import com.gov.reception.entity.WindowEntity;
import com.gov.reception.mapper.WindowMapper;
import com.gov.reception.service.WindowService;
import com.gov.reception.vo.WindowVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 窗口Service实现
 */
@Service
public class WindowServiceImpl extends ServiceImpl<WindowMapper, WindowEntity> implements WindowService {

    @Override
    public PageResult<WindowVO> pageQueryVO(Long pageNum, Long pageSize, Long deptId, String status) {
        LambdaQueryWrapper<WindowEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(deptId != null, WindowEntity::getDeptId, deptId);
        wrapper.eq(status != null, WindowEntity::getStatus, status);
        wrapper.orderByDesc(WindowEntity::getCreateTime);

        Page<WindowEntity> page = this.page(new Page<>(pageNum, pageSize), wrapper);
        List<WindowVO> voList = page.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        return PageResult.of(voList, page.getTotal(), page.getCurrent(), page.getSize());
    }

    @Override
    public WindowVO getVOById(Long id) {
        WindowEntity entity = this.getById(id);
        if (entity == null || entity.getDeleted() == 1) {
            return null;
        }
        return convertToVO(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addWindow(WindowEntity entity) {
        // 检查窗口编号是否重复
        LambdaQueryWrapper<WindowEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WindowEntity::getWindowNo, entity.getWindowNo());
        if (this.count(wrapper) > 0) {
            throw new BusinessException(400, "窗口编号已存在");
        }
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        this.save(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateWindow(WindowEntity entity) {
        WindowEntity exist = this.getById(entity.getId());
        if (exist == null || exist.getDeleted() == 1) {
            throw new BusinessException(404, "窗口不存在");
        }

        // 如果修改了编号，检查是否重复
        if (!exist.getWindowNo().equals(entity.getWindowNo())) {
            LambdaQueryWrapper<WindowEntity> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(WindowEntity::getWindowNo, entity.getWindowNo());
            wrapper.ne(WindowEntity::getId, entity.getId());
            if (this.count(wrapper) > 0) {
                throw new BusinessException(400, "窗口编号已存在");
            }
        }

        this.updateById(entity);
    }

    /**
     * Entity转VO
     */
    private WindowVO convertToVO(WindowEntity entity) {
        WindowVO vo = new WindowVO();
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
            case "0": return "关闭";
            case "1": return "开放";
            case "2": return "暂停";
            default: return "未知";
        }
    }
}
