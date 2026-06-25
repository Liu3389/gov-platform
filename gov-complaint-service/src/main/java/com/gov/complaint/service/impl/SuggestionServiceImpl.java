package com.gov.complaint.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gov.common.exception.BusinessException;
import com.gov.common.result.PageResult;
import com.gov.complaint.dto.SuggestionDTO;
import com.gov.complaint.entity.SuggestionEntity;
import com.gov.complaint.mapper.SuggestionMapper;
import com.gov.complaint.service.SuggestionService;
import com.gov.complaint.utils.UserContext;
import com.gov.complaint.vo.SuggestionVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SuggestionServiceImpl extends ServiceImpl<SuggestionMapper, SuggestionEntity> implements SuggestionService {

    @Override
    public PageResult<SuggestionVO> pageQueryVO(Long pageNum, Long pageSize, String keyword, String status) {
        LambdaQueryWrapper<SuggestionEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SuggestionEntity::getDeleted, 0);
        wrapper.like(StrUtil.isNotBlank(keyword), SuggestionEntity::getTitle, keyword);
        wrapper.eq(StrUtil.isNotBlank(status), SuggestionEntity::getStatus, status);
        wrapper.orderByDesc(SuggestionEntity::getCreateTime);
        Page<SuggestionEntity> page = this.page(new Page<>(pageNum, pageSize), wrapper);
        List<SuggestionVO> voList = page.getRecords().stream().map(this::toVO).collect(Collectors.toList());
        return PageResult.of(voList, page.getTotal(), page.getCurrent(), page.getSize());
    }

    @Override
    public SuggestionVO getVOById(Long id) {
        SuggestionEntity entity = this.getById(id);
        if (entity == null || entity.getDeleted() == 1) {
            throw new BusinessException(404, "建议不存在");
        }
        return toVO(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addSuggestion(SuggestionDTO dto) {
        SuggestionEntity entity = dto.toEntity();
        entity.setSuggestionNo(generateSuggestionNo());
        entity.setStatus("0");
        // 从网关Header自动填充当前用户信息
        entity.setUserId(UserContext.getUserId());
        entity.setUserName(UserContext.getUsername());
        this.save(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSuggestion(SuggestionDTO dto) {
        SuggestionEntity entity = this.getById(dto.getId());
        if (entity == null || entity.getDeleted() == 1) {
            throw new BusinessException(404, "建议不存在");
        }
        BeanUtil.copyProperties(dto, entity, "id", "suggestionNo", "createTime", "createBy", "deleted");
        this.updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void replySuggestion(Long id, String replyContent) {
        SuggestionEntity entity = this.getById(id);
        if (entity == null || entity.getDeleted() == 1) {
            throw new BusinessException(404, "建议不存在");
        }
        entity.setReplyContent(replyContent);
        // 从网关Header自动填充当前回复人信息
        entity.setReplyBy(UserContext.getUserId());
        entity.setReplyTime(LocalDateTime.now());
        entity.setStatus("1");
        this.updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteSuggestion(Long id) {
        SuggestionEntity entity = this.getById(id);
        if (entity == null || entity.getDeleted() == 1) {
            throw new BusinessException(404, "建议不存在");
        }
        this.removeById(id);
    }

    private SuggestionVO toVO(SuggestionEntity entity) {
        SuggestionVO vo = new SuggestionVO();
        BeanUtil.copyProperties(entity, vo);
        return vo;
    }

    private String generateSuggestionNo() {
        String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        int random = (int) (Math.random() * 9000) + 1000;
        return "SG" + dateStr + random;
    }
}
