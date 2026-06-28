package com.gov.message.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gov.common.result.PageResult;
import com.gov.message.dto.InboxDTO;
import com.gov.message.entity.InboxEntity;
import com.gov.message.vo.InboxVO;

public interface InboxService extends IService<InboxEntity> {

    PageResult<InboxVO> pageQueryVO(Long pageNum, Long pageSize, Long userId, Integer isRead);

    InboxVO getVOById(Long id);

    void addInbox(InboxDTO dto);

    void updateInbox(InboxDTO dto);

    void deleteInbox(Long id);

    void markAsRead(Long id);
}
