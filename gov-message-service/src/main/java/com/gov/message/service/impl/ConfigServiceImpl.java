package com.gov.message.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gov.common.exception.BusinessException;
import com.gov.common.result.PageResult;
import com.gov.message.dto.ConfigDTO;
import com.gov.message.entity.ConfigEntity;
import com.gov.message.mapper.ConfigMapper;
import com.gov.message.service.ConfigService;
import com.gov.message.vo.ConfigVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConfigServiceImpl extends ServiceImpl<ConfigMapper, ConfigEntity> implements ConfigService {

    @Override
    public PageResult<ConfigVO> pageQueryVO(Long pageNum, Long pageSize, String channel, String status) {
        LambdaQueryWrapper<ConfigEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ConfigEntity::getDeleted, 0);
        wrapper.eq(StrUtil.isNotBlank(channel), ConfigEntity::getChannel, channel);
        wrapper.eq(StrUtil.isNotBlank(status), ConfigEntity::getStatus, status);
        wrapper.orderByDesc(ConfigEntity::getCreateTime);
        Page<ConfigEntity> page = this.page(new Page<>(pageNum, pageSize), wrapper);
        List<ConfigVO> voList = page.getRecords().stream()
                .map(this::toVO)
                .collect(Collectors.toList());
        return PageResult.of(voList, page.getTotal(), page.getCurrent(), page.getSize());
    }

    @Override
    public ConfigVO getVOById(Long id) {
        ConfigEntity entity = this.getById(id);
        if (entity == null || entity.getDeleted() == 1) {
            throw new BusinessException(404, "配置不存在");
        }
        return toVO(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addConfig(ConfigDTO dto) {
        ConfigEntity entity = dto.toEntity();
        this.save(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateConfig(ConfigDTO dto) {
        ConfigEntity entity = this.getById(dto.getId());
        if (entity == null || entity.getDeleted() == 1) {
            throw new BusinessException(404, "配置不存在");
        }
        BeanUtil.copyProperties(dto, entity, "id", "createTime", "createBy", "deleted");
        this.updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteConfig(Long id) {
        ConfigEntity entity = this.getById(id);
        if (entity == null || entity.getDeleted() == 1) {
            throw new BusinessException(404, "配置不存在");
        }
        this.removeById(id);
    }

    private ConfigVO toVO(ConfigEntity entity) {
        ConfigVO vo = new ConfigVO();
        BeanUtil.copyProperties(entity, vo);
        // 敏感字段脱敏
        vo.setConfigValue("******");
        return vo;
    }
}
