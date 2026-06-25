package com.gov.complaint.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gov.common.exception.BusinessException;
import com.gov.common.result.PageResult;
import com.gov.complaint.dto.HotspotDTO;
import com.gov.complaint.entity.HotspotEntity;
import com.gov.complaint.mapper.HotspotMapper;
import com.gov.complaint.service.HotspotService;
import com.gov.complaint.vo.HotspotVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HotspotServiceImpl extends ServiceImpl<HotspotMapper, HotspotEntity> implements HotspotService {

    @Override
    public PageResult<HotspotVO> pageQueryVO(Long pageNum, Long pageSize, String keyword) {
        LambdaQueryWrapper<HotspotEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(HotspotEntity::getDeleted, 0);
        wrapper.like(StrUtil.isNotBlank(keyword), HotspotEntity::getKeyword, keyword);
        wrapper.orderByDesc(HotspotEntity::getKeywordCount);
        Page<HotspotEntity> page = this.page(new Page<>(pageNum, pageSize), wrapper);
        List<HotspotVO> voList = page.getRecords().stream().map(this::toVO).collect(Collectors.toList());
        return PageResult.of(voList, page.getTotal(), page.getCurrent(), page.getSize());
    }

    @Override
    public HotspotVO getVOById(Long id) {
        HotspotEntity entity = this.getById(id);
        if (entity == null || entity.getDeleted() == 1) {
            throw new BusinessException(404, "热点数据不存在");
        }
        return toVO(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addHotspot(HotspotDTO dto) {
        HotspotEntity entity = dto.toEntity();
        this.save(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateHotspot(HotspotDTO dto) {
        HotspotEntity entity = this.getById(dto.getId());
        if (entity == null || entity.getDeleted() == 1) {
            throw new BusinessException(404, "热点数据不存在");
        }
        BeanUtil.copyProperties(dto, entity, "id", "createTime", "createBy", "deleted");
        this.updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteHotspot(Long id) {
        HotspotEntity entity = this.getById(id);
        if (entity == null || entity.getDeleted() == 1) {
            throw new BusinessException(404, "热点数据不存在");
        }
        this.removeById(id);
    }

    private HotspotVO toVO(HotspotEntity entity) {
        HotspotVO vo = new HotspotVO();
        BeanUtil.copyProperties(entity, vo);
        return vo;
    }
}
