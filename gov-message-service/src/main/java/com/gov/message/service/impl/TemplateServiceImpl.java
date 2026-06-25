package com.gov.message.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gov.common.exception.BusinessException;
import com.gov.common.result.PageResult;
import com.gov.message.dto.TemplateDTO;
import com.gov.message.entity.TemplateEntity;
import com.gov.message.mapper.TemplateMapper;
import com.gov.message.service.TemplateService;
import com.gov.message.vo.TemplateVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TemplateServiceImpl extends ServiceImpl<TemplateMapper, TemplateEntity> implements TemplateService {

    @Override
    public PageResult<TemplateVO> pageQueryVO(Long pageNum, Long pageSize, String keyword, String channel, String status) {
        LambdaQueryWrapper<TemplateEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TemplateEntity::getDeleted, 0);
        wrapper.like(StrUtil.isNotBlank(keyword), TemplateEntity::getTemplateName, keyword);
        wrapper.eq(StrUtil.isNotBlank(channel), TemplateEntity::getChannel, channel);
        wrapper.eq(StrUtil.isNotBlank(status), TemplateEntity::getStatus, status);
        wrapper.orderByDesc(TemplateEntity::getCreateTime);
        Page<TemplateEntity> page = this.page(new Page<>(pageNum, pageSize), wrapper);
        List<TemplateVO> voList = page.getRecords().stream()
                .map(this::toVO)
                .collect(Collectors.toList());
        return PageResult.of(voList, page.getTotal(), page.getCurrent(), page.getSize());
    }

    @Override
    public TemplateVO getVOById(Long id) {
        TemplateEntity entity = this.getById(id);
        if (entity == null || entity.getDeleted() == 1) {
            throw new BusinessException(404, "模板不存在");
        }
        return toVO(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addTemplate(TemplateDTO dto) {
        TemplateEntity entity = dto.toEntity();
        this.save(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateTemplate(TemplateDTO dto) {
        TemplateEntity entity = this.getById(dto.getId());
        if (entity == null || entity.getDeleted() == 1) {
            throw new BusinessException(404, "模板不存在");
        }
        BeanUtil.copyProperties(dto, entity, "id", "templateCode", "createTime", "createBy", "deleted");
        this.updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTemplate(Long id) {
        TemplateEntity entity = this.getById(id);
        if (entity == null || entity.getDeleted() == 1) {
            throw new BusinessException(404, "模板不存在");
        }
        this.removeById(id);
    }

    /**
     * Entity → VO 转换
     */
    private TemplateVO toVO(TemplateEntity entity) {
        TemplateVO vo = new TemplateVO();
        BeanUtil.copyProperties(entity, vo);
        return vo;
    }
}
