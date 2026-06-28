package com.gov.message.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gov.common.result.PageResult;
import com.gov.message.dto.ConfigDTO;
import com.gov.message.entity.ConfigEntity;
import com.gov.message.vo.ConfigVO;

public interface ConfigService extends IService<ConfigEntity> {

    PageResult<ConfigVO> pageQueryVO(Long pageNum, Long pageSize, String channel, String status);

    ConfigVO getVOById(Long id);

    void addConfig(ConfigDTO dto);

    void updateConfig(ConfigDTO dto);

    void deleteConfig(Long id);
}
