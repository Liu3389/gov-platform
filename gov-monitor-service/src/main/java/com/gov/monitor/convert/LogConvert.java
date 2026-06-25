package com.gov.monitor.convert;

import com.gov.monitor.dto.LogDTO;
import com.gov.monitor.entity.LogEntity;
import com.gov.monitor.vo.LogVO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class LogConvert {

    public static LogEntity toEntity(LogDTO dto) {
        if (dto == null) return null;
        LogEntity entity = new LogEntity();
        entity.setUserId(dto.getUserId());
        entity.setUserName(dto.getUserName());
        entity.setDeptId(dto.getDeptId());
        entity.setDeptName(dto.getDeptName());
        entity.setModule(dto.getModule());
        entity.setAction(dto.getAction());
        entity.setMethod(dto.getMethod());
        entity.setRequestUrl(dto.getRequestUrl());
        entity.setRequestType(dto.getRequestType());
        entity.setRequestParams(dto.getRequestParams());
        entity.setResponseData(dto.getResponseData());
        entity.setOperateIp(dto.getOperateIp());
        entity.setOperateLocation(dto.getOperateLocation());
        entity.setExecuteTime(dto.getExecuteTime());
        entity.setStatus(dto.getStatus());
        entity.setErrorMsg(dto.getErrorMsg());
        return entity;
    }

    public static LogVO toVO(LogEntity entity) {
        if (entity == null) return null;
        LogVO vo = new LogVO();
        vo.setId(entity.getId());
        vo.setUserId(entity.getUserId());
        vo.setUserName(entity.getUserName());
        vo.setDeptId(entity.getDeptId());
        vo.setDeptName(entity.getDeptName());
        vo.setModule(entity.getModule());
        vo.setAction(entity.getAction());
        vo.setMethod(entity.getMethod());
        vo.setRequestUrl(entity.getRequestUrl());
        vo.setRequestType(entity.getRequestType());
        vo.setRequestParams(entity.getRequestParams());
        vo.setResponseData(entity.getResponseData());
        vo.setOperateTime(entity.getOperateTime());
        vo.setOperateIp(entity.getOperateIp());
        vo.setOperateLocation(entity.getOperateLocation());
        vo.setExecuteTime(entity.getExecuteTime());
        vo.setStatus(entity.getStatus());
        vo.setErrorMsg(entity.getErrorMsg());
        vo.setCreateTime(entity.getCreateTime());
        return vo;
    }

    public static List<LogVO> toVOList(List<LogEntity> entities) {
        if (entities == null) return null;
        return entities.stream()
                .map(LogConvert::toVO)
                .collect(Collectors.toList());
    }
}
