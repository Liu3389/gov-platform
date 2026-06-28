package com.gov.datashare.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gov.common.result.PageResult;
import com.gov.datashare.dto.DataSourceDTO;
import com.gov.datashare.entity.DataSourceEntity;
import com.gov.datashare.vo.DataSourceVO;

public interface DataSourceService extends IService<DataSourceEntity> {

    PageResult<DataSourceVO> pageQueryVO(Long pageNum, Long pageSize, String keyword, String status);

    DataSourceVO getVOById(Long id);

    void addDataSource(DataSourceDTO dto);

    void updateDataSource(DataSourceDTO dto);

    void deleteDataSource(Long id);
}
