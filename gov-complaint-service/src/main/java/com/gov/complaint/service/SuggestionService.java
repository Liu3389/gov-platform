package com.gov.complaint.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gov.common.result.PageResult;
import com.gov.complaint.dto.SuggestionDTO;
import com.gov.complaint.entity.SuggestionEntity;
import com.gov.complaint.vo.SuggestionVO;

public interface SuggestionService extends IService<SuggestionEntity> {
    PageResult<SuggestionVO> pageQueryVO(Long pageNum, Long pageSize, String keyword, String status);
    SuggestionVO getVOById(Long id);
    void addSuggestion(SuggestionDTO dto);
    void updateSuggestion(SuggestionDTO dto);
    void replySuggestion(Long id, String replyContent);
    void deleteSuggestion(Long id);
}
