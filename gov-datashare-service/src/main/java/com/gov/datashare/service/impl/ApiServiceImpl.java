package com.gov.datashare.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gov.common.exception.BusinessException;
import com.gov.common.result.PageResult;
import com.gov.datashare.dto.ApiDTO;
import com.gov.datashare.entity.ApiEntity;
import com.gov.datashare.mapper.ApiMapper;
import com.gov.datashare.service.ApiService;
import com.gov.datashare.vo.ApiVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ApiServiceImpl extends ServiceImpl<ApiMapper, ApiEntity> implements ApiService {

    @Override
    public PageResult<ApiVO> pageQueryVO(Long pageNum, Long pageSize, String keyword, String status) {
        LambdaQueryWrapper<ApiEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ApiEntity::getDeleted, 0);
        wrapper.like(StrUtil.isNotBlank(keyword), ApiEntity::getApiName, keyword);
        wrapper.eq(StrUtil.isNotBlank(status), ApiEntity::getStatus, status);
        wrapper.orderByDesc(ApiEntity::getCreateTime);
        Page<ApiEntity> page = this.page(new Page<>(pageNum, pageSize), wrapper);
        List<ApiVO> voList = page.getRecords().stream()
                .map(this::toVO)
                .collect(Collectors.toList());
        return PageResult.of(voList, page.getTotal(), page.getCurrent(), page.getSize());
    }

    @Override
    public ApiVO getVOById(Long id) {
        ApiEntity entity = this.getById(id);
        if (entity == null || entity.getDeleted() == 1) {
            throw new BusinessException(404, "接口不存在");
        }
        return toVO(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addApi(ApiDTO dto) {
        ApiEntity entity = dto.toEntity();
        this.save(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateApi(ApiDTO dto) {
        ApiEntity entity = this.getById(dto.getId());
        if (entity == null || entity.getDeleted() == 1) {
            throw new BusinessException(404, "接口不存在");
        }
        BeanUtil.copyProperties(dto, entity, "id", "apiCode", "createTime", "createBy", "deleted");
        this.updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteApi(Long id) {
        ApiEntity entity = this.getById(id);
        if (entity == null || entity.getDeleted() == 1) {
            throw new BusinessException(404, "接口不存在");
        }
        this.removeById(id);
    }

    /**
     * Entity → VO 转换
     */
    private ApiVO toVO(ApiEntity entity) {
        ApiVO vo = new ApiVO();
        BeanUtil.copyProperties(entity, vo);
        return vo;
    }
}
