package com.gov.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gov.common.result.PageResult;
import com.gov.user.dto.DictDTO;
import com.gov.user.entity.DictEntity;
import com.gov.user.vo.DictVO;

import java.util.List;

public interface DictService extends IService<DictEntity> {

    List<DictVO> listVOByType(String dictType);

    PageResult<DictVO> pageQueryVO(Long pageNum, Long pageSize, String dictType);

    DictVO getVOById(Long id);

    void addDict(DictDTO dto);

    void updateDict(DictDTO dto);

    void deleteDict(Long id);
}
