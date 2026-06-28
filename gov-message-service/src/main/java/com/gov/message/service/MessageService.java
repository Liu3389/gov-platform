package com.gov.message.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gov.common.result.PageResult;
import com.gov.message.dto.MessageSendDTO;
import com.gov.message.entity.RecordEntity;
import com.gov.message.vo.MessageSendVO;
import com.gov.message.vo.RecordVO;

/**
 * 消息发送服务接口
 */
public interface MessageService extends IService<RecordEntity> {

    /**
     * 发送消息
     *
     * @param dto 消息发送请求
     * @return 发送结果
     */
    MessageSendVO sendMessage(MessageSendDTO dto);

    /**
     * 分页查询消息记录
     *
     * @param pageNum    页码
     * @param pageSize   每页大小
     * @param channel    发送渠道
     * @param sendStatus 发送状态
     * @return 分页结果
     */
    PageResult<RecordVO> pageQueryRecord(Long pageNum, Long pageSize, String channel, String sendStatus);

    /**
     * 查询未读消息数
     *
     * @param userId 用户ID
     * @return 未读消息数
     */
    Long countUnread(Long userId);
}
