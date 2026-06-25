package com.gov.complaint.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gov.common.result.PageResult;
import com.gov.complaint.dto.HotspotDTO;
import com.gov.complaint.entity.HotspotEntity;
import com.gov.complaint.vo.HotspotVO;

public interface HotspotService extends IService<HotspotEntity> {
    PageResult<HotspotVO> pageQueryVO(Long pageNum, Long pageSize, String keyword);
    HotspotVO getVOById(Long id);
    void addHotspot(HotspotDTO dto);
    void updateHotspot(HotspotDTO dto);
    void deleteHotspot(Long id);
}
