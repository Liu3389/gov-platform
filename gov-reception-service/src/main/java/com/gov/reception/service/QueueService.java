package com.gov.reception.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gov.common.result.PageResult;
import com.gov.reception.entity.QueueEntity;
import com.gov.reception.vo.QueueVO;

/**
 * 排队叫号Service
 */
public interface QueueService extends IService<QueueEntity> {

    /**
     * 分页查询排队
     */
    PageResult<QueueVO> pageQueryVO(Long pageNum, Long pageSize, Long windowId, Integer status);

    /**
     * 取号排队
     */
    QueueVO takeQueue(Long userId, Long itemId);

    /**
     * 叫号
     */
    void callQueue(Long id, Long windowId);

    /**
     * 完成办理
     */
    void finishQueue(Long id);

    /**
     * 取消排队
     */
    void cancelQueue(Long id);

    /**
     * 查询前面等待人数
     */
    int getWaitCount(Long id);
}
