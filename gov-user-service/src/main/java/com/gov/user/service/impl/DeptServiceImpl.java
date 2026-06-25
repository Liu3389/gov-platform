package com.gov.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gov.common.exception.BusinessException;
import com.gov.common.result.PageResult;
import com.gov.user.convert.UserConvert;
import com.gov.user.dto.DeptDTO;
import com.gov.user.entity.DeptEntity;
import com.gov.user.mapper.DeptMapper;
import com.gov.user.service.DeptService;
import com.gov.user.vo.DeptVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeptServiceImpl extends ServiceImpl<DeptMapper, DeptEntity> implements DeptService {

    @Override
    public List<DeptEntity> listAll() {
        return this.lambdaQuery()
                .eq(DeptEntity::getDeleted, 0)
                .orderByAsc(DeptEntity::getSort)
                .list();
    }

    @Override
    public List<DeptVO> listAllVO() {
        List<DeptEntity> entities = listAll();
        return UserConvert.toDeptVOList(entities);
    }

    @Override
    public PageResult<DeptEntity> pageQuery(Long pageNum, Long pageSize, String deptName) {
        LambdaQueryWrapper<DeptEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DeptEntity::getDeleted, 0);
        if (StringUtils.hasText(deptName)) {
            wrapper.like(DeptEntity::getDeptName, deptName);
        }
        wrapper.orderByAsc(DeptEntity::getSort);
        var page = this.page(new Page<>(pageNum, pageSize), wrapper);
        return PageResult.of(page.getRecords(), page.getTotal(), page.getCurrent(), page.getSize());
    }

    @Override
    public PageResult<DeptVO> pageQueryVO(Long pageNum, Long pageSize, String deptName) {
        PageResult<DeptEntity> entityPage = pageQuery(pageNum, pageSize, deptName);
        List<DeptVO> voList = UserConvert.toDeptVOList(entityPage.getRecords());
        return PageResult.of(voList, entityPage.getTotal(), entityPage.getPageNum(), entityPage.getPageSize());
    }

    @Override
    public DeptEntity getByDeptCode(String deptCode) {
        return this.lambdaQuery()
                .eq(DeptEntity::getDeleted, 0)
                .eq(DeptEntity::getDeptCode, deptCode)
                .one();
    }

    @Override
    public DeptVO getVOByDeptCode(String deptCode) {
        DeptEntity entity = getByDeptCode(deptCode);
        if (entity == null) {
            throw BusinessException.notFound("部门不存在");
        }
        return UserConvert.toVO(entity);
    }

    @Override
    public DeptVO getVOById(Long id) {
        DeptEntity entity = this.getById(id);
        if (entity == null) {
            throw BusinessException.notFound("部门不存在");
        }
        return UserConvert.toVO(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addDept(DeptDTO dto) {
        DeptEntity entity = UserConvert.toEntity(dto);
        this.save(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDept(DeptDTO dto) {
        DeptEntity exist = this.getById(dto.getId());
        if (exist == null) {
            throw BusinessException.notFound("部门不存在");
        }
        DeptEntity entity = UserConvert.toEntity(dto);
        entity.setId(dto.getId());
        this.updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDept(Long id) {
        DeptEntity entity = this.getById(id);
        if (entity == null) {
            throw BusinessException.notFound("部门不存在");
        }
        this.removeById(id);
    }
}
