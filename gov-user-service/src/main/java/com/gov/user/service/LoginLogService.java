package com.gov.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gov.common.result.PageResult;
import com.gov.user.dto.LoginLogDTO;
import com.gov.user.entity.LoginLogEntity;
import com.gov.user.vo.LoginLogVO;

public interface LoginLogService extends IService<LoginLogEntity> {

    PageResult<LoginLogVO> pageQueryVO(Long pageNum, Long pageSize, Long userId);

    LoginLogVO getVOById(Long id);

    void recordLogin(LoginLogDTO dto);
}
