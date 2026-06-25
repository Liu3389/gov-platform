package com.gov.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gov.common.result.PageResult;
import com.gov.user.dto.UserRealnameDTO;
import com.gov.user.entity.UserRealnameEntity;
import com.gov.user.vo.UserRealnameVO;

public interface UserRealnameService extends IService<UserRealnameEntity> {

    UserRealnameVO getVOByUserId(Long userId);

    PageResult<UserRealnameVO> pageQueryVO(Long pageNum, Long pageSize, Integer verifyStatus);

    void submitRealname(UserRealnameDTO dto);

    void verifyRealname(Long id, Integer verifyStatus, String verifyRemark);
}
