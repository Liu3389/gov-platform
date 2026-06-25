package com.gov.message.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gov.common.result.PageResult;
import com.gov.message.dto.TemplateDTO;
import com.gov.message.entity.TemplateEntity;
import com.gov.message.vo.TemplateVO;

public interface TemplateService extends IService<TemplateEntity> {

    PageResult<TemplateVO> pageQueryVO(Long pageNum, Long pageSize, String keyword, String channel, String status);

    TemplateVO getVOById(Long id);

    void addTemplate(TemplateDTO dto);

    void updateTemplate(TemplateDTO dto);

    void deleteTemplate(Long id);
}
