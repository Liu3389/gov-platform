package com.gov.datashare.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gov.common.exception.BusinessException;
import com.gov.common.result.PageResult;
import com.gov.datashare.dto.DataSourceDTO;
import com.gov.datashare.entity.DataSourceEntity;
import com.gov.datashare.mapper.DataSourceMapper;
import com.gov.datashare.service.DataSourceService;
import com.gov.datashare.vo.DataSourceVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DataSourceServiceImpl extends ServiceImpl<DataSourceMapper, DataSourceEntity> implements DataSourceService {

    @Override
    public PageResult<DataSourceVO> pageQueryVO(Long pageNum, Long pageSize, String keyword, String status) {
        LambdaQueryWrapper<DataSourceEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DataSourceEntity::getDeleted, 0);
        wrapper.like(StrUtil.isNotBlank(keyword), DataSourceEntity::getSourceName, keyword);
        wrapper.eq(StrUtil.isNotBlank(status), DataSourceEntity::getStatus, status);
        wrapper.orderByDesc(DataSourceEntity::getCreateTime);
        Page<DataSourceEntity> page = this.page(new Page<>(pageNum, pageSize), wrapper);
        List<DataSourceVO> voList = page.getRecords().stream()
                .map(this::toVO)
                .collect(Collectors.toList());
        return PageResult.of(voList, page.getTotal(), page.getCurrent(), page.getSize());
    }

    @Override
    public DataSourceVO getVOById(Long id) {
        DataSourceEntity entity = this.getById(id);
        if (entity == null || entity.getDeleted() == 1) {
            throw new BusinessException(404, "数据源不存在");
        }
        return toVO(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addDataSource(DataSourceDTO dto) {
        DataSourceEntity entity = dto.toEntity();
        this.save(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDataSource(DataSourceDTO dto) {
        DataSourceEntity entity = this.getById(dto.getId());
        if (entity == null || entity.getDeleted() == 1) {
            throw new BusinessException(404, "数据源不存在");
        }
        BeanUtil.copyProperties(dto, entity, "id", "sourceCode", "createTime", "createBy", "deleted");
        this.updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDataSource(Long id) {
        DataSourceEntity entity = this.getById(id);
        if (entity == null || entity.getDeleted() == 1) {
            throw new BusinessException(404, "数据源不存在");
        }
        this.removeById(id);
    }

    private DataSourceVO toVO(DataSourceEntity entity) {
        DataSourceVO vo = new DataSourceVO();
        BeanUtil.copyProperties(entity, vo);
        // 敏感字段脱敏
        vo.setDbPassword("******");
        vo.setApiKey("******");
        return vo;
    }
}
