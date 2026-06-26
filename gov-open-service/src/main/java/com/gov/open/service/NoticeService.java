package com.gov.open.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gov.common.result.PageResult;
import com.gov.open.dto.NoticeDTO;
import com.gov.open.dto.NoticeQueryDTO;
import com.gov.open.entity.NoticeEntity;
import com.gov.open.vo.NoticeVO;

/**
 * 通知公告Service
 */
public interface NoticeService extends IService<NoticeEntity> {

    /**
     * 分页查询公告VO
     */
    PageResult<NoticeVO> pageQueryVO(Long pageNum, Long pageSize, NoticeQueryDTO queryDTO);

    /**
     * 根据ID查询公告VO
     */
    NoticeVO getVOById(Long id);

    /**
     * 新增公告
     */
    void addNotice(NoticeDTO dto, Long publishDeptId);

    /**
     * 更新公告
     */
    void updateNotice(NoticeDTO dto, Long id);

    /**
     * 发布公告
     */
    void publishNotice(Long id);

    /**
     * 撤回公告
     */
    void withdrawNotice(Long id);

    /**
     * 增加浏览次数
     */
    void incrementViewCount(Long id);
}
