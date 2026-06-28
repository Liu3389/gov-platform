package com.gov.license.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gov.common.constant.WorkflowConstants;
import com.gov.common.exception.BusinessException;
import com.gov.common.result.PageResult;
import com.gov.common.result.Result;
import com.gov.license.dto.*;
import com.gov.license.entity.*;
import com.gov.license.feign.MessageFeignClient;
import com.gov.license.mapper.*;
import com.gov.license.service.LicenseService;
import com.gov.license.vo.LicenseDetailVO;
import com.gov.license.vo.LicenseVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class LicenseServiceImpl extends ServiceImpl<LicenseMapper, LicenseEntity> implements LicenseService {

  private final LicenseCatalogMapper catalogMapper;
  private final LicenseSignMapper signMapper;
  private final LicenseVerifyMapper verifyMapper;
  private final LicenseAuthMapper authMapper;
  private final MessageFeignClient messageFeignClient;

  @Override
  public PageResult<LicenseVO> pageQuery(Long pageNum, Long pageSize, String licenseNo, String keyword, String status) {
    LambdaQueryWrapper<LicenseEntity> wrapper = new LambdaQueryWrapper<>();
    wrapper.eq(LicenseEntity::getDeleted, 0);
    wrapper.eq(StrUtil.isNotBlank(licenseNo), LicenseEntity::getLicenseNo, licenseNo);
    wrapper.eq(StrUtil.isNotBlank(status), LicenseEntity::getStatus, status);
    wrapper.and(StrUtil.isNotBlank(keyword), w -> w
        .like(LicenseEntity::getUserName, keyword)
        .or().like(LicenseEntity::getCatalogName, keyword));
    wrapper.orderByDesc(LicenseEntity::getCreateTime);

    Page<LicenseEntity> page = this.page(new Page<>(pageNum, pageSize), wrapper);
    List<LicenseVO> voList = page.getRecords().stream().map(e -> {
      LicenseVO vo = new LicenseVO();
      BeanUtil.copyProperties(e, vo);
      return vo;
    }).collect(Collectors.toList());
    return new PageResult<>(voList, page.getTotal(), page.getCurrent(), page.getSize());
  }

  @Override
  public LicenseDetailVO getDetailById(Long id) {
    LicenseEntity entity = this.getById(id);
    if (entity == null || entity.getDeleted() == 1) {
      throw BusinessException.notFound("证照不存在");
    }
    LicenseDetailVO vo = new LicenseDetailVO();
    BeanUtil.copyProperties(entity, vo);
    return vo;
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public LicenseVO generate(LicenseGenerateDTO dto) {
    // 查证照目录
    LicenseCatalogEntity catalog = catalogMapper.selectOne(
        new LambdaQueryWrapper<LicenseCatalogEntity>()
            .eq(LicenseCatalogEntity::getCatalogCode, dto.getCatalogCode())
            .eq(LicenseCatalogEntity::getDeleted, 0));
    if (catalog == null) {
      throw new BusinessException(404, "证照目录不存在：" + dto.getCatalogCode());
    }

    // 生成证照编号：LIC + 年月日 + 6位随机
    String licenseNo = "LIC" + LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd"))
        + IdUtil.fastSimpleUUID().substring(0, 6).toUpperCase();

    LicenseEntity entity = new LicenseEntity();
    entity.setLicenseNo(licenseNo);
    entity.setCatalogId(catalog.getId());
    entity.setCatalogName(catalog.getCatalogName());
    entity.setUserId(dto.getUserId());
    entity.setUserName(dto.getUserName());
    entity.setApplyNo(dto.getApplyNo());
    entity.setStatus("1");
    entity.setExpireTime(catalog.getValidYears() != null
        ? LocalDateTime.now().plusYears(catalog.getValidYears())
        : null);
    this.save(entity);

    log.info("证照生成成功：licenseNo={}, userId={}, catalogCode={}", licenseNo, dto.getUserId(), dto.getCatalogCode());

    // ★ 证照生成成功后发送消息通知
    sendNotification(entity, dto);

    LicenseVO vo = new LicenseVO();
    BeanUtil.copyProperties(entity, vo);
    return vo;
  }

  /** 发送证照生成通知 */
  private void sendNotification(LicenseEntity entity, LicenseGenerateDTO dto) {
    try {
      MessageSendDTO msgDTO = MessageSendDTO.builder()
          .title("证照办理完成通知")
          .content("您的" + dto.getItemName() + "证照已生成，证照编号：" + entity.getLicenseNo())
          .receiverId(dto.getUserId())
          .channel("INNER")
          .licenseNo(entity.getLicenseNo())
          .itemName(dto.getItemName())
          .completeTime(LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
          .build();
      Result<Void> result = messageFeignClient.send(msgDTO);
      log.info("消息通知发送结果：code={}, licenseNo={}", result.getCode(), entity.getLicenseNo());
    } catch (Exception e) {
      log.error("发送消息通知失败（不阻塞证照生成）：licenseNo={}, error={}",
          entity.getLicenseNo(), e.getMessage(), e);
    }
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void sign(LicenseSignDTO dto, Long signUserId) {
    LicenseEntity entity = this.getById(dto.getLicenseId());
    if (entity == null || entity.getDeleted() == 1) {
      throw BusinessException.notFound("证照不存在");
    }
    if (!"1".equals(entity.getStatus())) {
      throw new BusinessException(400, "当前证照状态不允许签章");
    }

    // 记录签章
    LicenseSignEntity sign = new LicenseSignEntity();
    sign.setLicenseId(dto.getLicenseId());
    sign.setSignType(dto.getSignType());
    sign.setSignUser(signUserId);
    sign.setSignTime(LocalDateTime.now());
    sign.setSignResult("SUCCESS");
    signMapper.insert(sign);

    // 更新证照签章信息
    entity.setSignTime(LocalDateTime.now());
    entity.setSignBy(signUserId);
    this.updateById(entity);

    log.info("证照签章成功：licenseId={}, signUser={}", dto.getLicenseId(), signUserId);
  }

  @Override
  public LicenseDetailVO verify(LicenseVerifyDTO dto, String verifyIp) {
    LicenseEntity entity = this.lambdaQuery()
        .eq(LicenseEntity::getLicenseNo, dto.getLicenseNo())
        .eq(LicenseEntity::getDeleted, 0)
        .one();
    if (entity == null) {
      throw BusinessException.notFound("证照不存在");
    }

    // 记录核验日志
    LicenseVerifyEntity verify = new LicenseVerifyEntity();
    verify.setLicenseId(entity.getId());
    verify.setLicenseNo(entity.getLicenseNo());
    verify.setVerifyUser(dto.getVerifyUserId() != null ? dto.getVerifyUserId() : 0L);
    verify.setVerifyTime(LocalDateTime.now());
    verify.setVerifyResult(entity.getStatus());
    verify.setVerifyScene(dto.getVerifyScene());
    verify.setVerifyIp(verifyIp);
    verifyMapper.insert(verify);

    LicenseDetailVO vo = new LicenseDetailVO();
    BeanUtil.copyProperties(entity, vo);
    return vo;
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void auth(LicenseAuthDTO dto, Long authUserId) {
    LicenseEntity entity = this.getById(dto.getLicenseId());
    if (entity == null || entity.getDeleted() == 1) {
      throw BusinessException.notFound("证照不存在");
    }

    LicenseAuthEntity auth = new LicenseAuthEntity();
    auth.setLicenseId(dto.getLicenseId());
    auth.setAuthUserId(authUserId);
    auth.setAuthTargetId(dto.getAuthTargetId());
    auth.setAuthTargetName(dto.getAuthTargetName());
    auth.setAuthType(dto.getAuthType());
    auth.setAuthTime(LocalDateTime.now());
    auth.setStatus("1");
    authMapper.insert(auth);

    log.info("证照授权成功：licenseId={}, from={}, to={}", dto.getLicenseId(), authUserId, dto.getAuthTargetId());
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void updateLicense(Long id, LicenseGenerateDTO dto) {
    LicenseEntity entity = this.getById(id);
    if (entity == null || entity.getDeleted() == 1) {
      throw BusinessException.notFound("证照不存在");
    }
    entity.setCatalogName(dto.getItemName());
    entity.setUserId(dto.getUserId());
    entity.setUserName(dto.getUserName());
    entity.setApplyNo(dto.getApplyNo());
    this.updateById(entity);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void deleteLicense(Long id) {
    LicenseEntity entity = this.getById(id);
    if (entity == null || entity.getDeleted() == 1) {
      throw BusinessException.notFound("证照不存在");
    }
    this.removeById(id);
  }
}
