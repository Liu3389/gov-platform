package com.gov.message.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gov.common.exception.BusinessException;
import com.gov.common.result.PageResult;
import com.gov.message.dto.MessageSendDTO;
import com.gov.message.entity.InboxEntity;
import com.gov.message.entity.RecordEntity;
import com.gov.message.entity.TemplateEntity;
import com.gov.message.mapper.InboxMapper;
import com.gov.message.mapper.RecordMapper;
import com.gov.message.service.MessageService;
import com.gov.message.service.TemplateService;
import com.gov.message.utils.UserContext;
import com.gov.message.vo.MessageSendVO;
import com.gov.message.vo.RecordVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 消息发送服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MessageServiceImpl extends ServiceImpl<RecordMapper, RecordEntity> implements MessageService {

    private final TemplateService templateService;
    private final InboxMapper inboxMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MessageSendVO sendMessage(MessageSendDTO dto) {
        // 1. 查询消息模板
        TemplateEntity template = templateService.getById(dto.getTemplateId());
        if (template == null || template.getDeleted() == 1) {
            throw new BusinessException(404, "消息模板不存在");
        }

        // 2. 替换模板变量
        String content = replaceTemplateVariables(template.getTemplateContent(), dto.getVariables());

        // 3. 获取接收人信息（优先使用DTO传入的接收人姓名，否则使用当前登录用户）
        Long receiverId = dto.getReceiverId();
        String receiverName = dto.getReceiverName();
        if (StrUtil.isBlank(receiverName)) {
            receiverName = UserContext.getUsername();
            if (StrUtil.isBlank(receiverName)) {
                receiverName = "用户" + receiverId;
            }
        }

        // 4. 根据渠道发送消息（每个渠道创建独立的记录对象）
        List<String> channels = dto.getChannels();
        RecordEntity lastRecord = null;
        for (String channel : channels) {
            // 每个渠道创建新的 RecordEntity 对象，避免复用导致 UPDATE 而非 INSERT
            RecordEntity record = new RecordEntity();
            record.setTemplateId(template.getId());
            record.setTemplateCode(template.getTemplateCode());
            record.setReceiverId(receiverId);
            record.setReceiverName(receiverName);
            record.setContent(content);
            record.setChannel(channel);
            record.setBusinessType(dto.getBusinessType());
            record.setBusinessId(dto.getBusinessId());
            record.setSendTime(LocalDateTime.now());
            record.setSendStatus("2"); // 2-成功
            record.setSendMsg("发送成功");
            record.setRetryCount(0);
            
            this.save(record);
            lastRecord = record;

            // 根据渠道类型处理发送逻辑
            switch (channel) {
                case "SITE_MSG":
                    // 站内信：保存到站内信表
                    saveInboxMessage(record, template);
                    log.info("[消息发送] 站内信已保存，接收人：{}，内容：{}", receiverName, content);
                    break;
                case "SMS":
                    // 短信：模拟发送，打印日志
                    log.info("[消息发送] 短信模拟发送，接收人：{}，内容：{}", receiverName, content);
                    break;
                case "EMAIL":
                    // 邮件：模拟发送，打印日志
                    log.info("[消息发送] 邮件模拟发送，接收人：{}，内容：{}", receiverName, content);
                    break;
                case "APP_PUSH":
                    // APP推送：模拟发送，打印日志
                    log.info("[消息发送] APP推送模拟发送，接收人：{}，内容：{}", receiverName, content);
                    break;
                default:
                    log.warn("[消息发送] 未知渠道：{}", channel);
            }
        }

        // 5. 返回发送结果（使用最后一个渠道的记录）
        return toSendVO(lastRecord, channels);
    }

    @Override
    public PageResult<RecordVO> pageQueryRecord(Long pageNum, Long pageSize, String channel, String sendStatus) {
        LambdaQueryWrapper<RecordEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RecordEntity::getDeleted, 0);
        wrapper.eq(StrUtil.isNotBlank(channel), RecordEntity::getChannel, channel);
        wrapper.eq(StrUtil.isNotBlank(sendStatus), RecordEntity::getSendStatus, sendStatus);
        wrapper.orderByDesc(RecordEntity::getCreateTime);

        Page<RecordEntity> page = this.page(new Page<>(pageNum, pageSize), wrapper);
        List<RecordVO> voList = page.getRecords().stream()
                .map(this::toRecordVO)
                .collect(Collectors.toList());
        return PageResult.of(voList, page.getTotal(), page.getCurrent(), page.getSize());
    }

    @Override
    public Long countUnread(Long userId) {
        LambdaQueryWrapper<InboxEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InboxEntity::getDeleted, 0);
        wrapper.eq(InboxEntity::getUserId, userId);
        wrapper.eq(InboxEntity::getIsRead, 0);
        return inboxMapper.selectCount(wrapper);
    }

    /**
     * 替换模板变量
     */
    private String replaceTemplateVariables(String templateContent, java.util.Map<String, String> variables) {
        if (StrUtil.isBlank(templateContent) || variables == null || variables.isEmpty()) {
            return templateContent;
        }
        String content = templateContent;
        for (java.util.Map.Entry<String, String> entry : variables.entrySet()) {
            String placeholder = "${" + entry.getKey() + "}";
            content = content.replace(placeholder, entry.getValue());
        }
        return content;
    }

    /**
     * 保存站内信消息
     */
    private void saveInboxMessage(RecordEntity record, TemplateEntity template) {
        InboxEntity inbox = new InboxEntity();
        inbox.setUserId(record.getReceiverId());
        inbox.setTitle(template.getTemplateName());
        inbox.setContent(record.getContent());
        inbox.setMsgType(template.getTemplateType());
        inbox.setBusinessType(record.getBusinessType());
        inbox.setBusinessId(record.getBusinessId());
        inbox.setIsRead(0);
        inbox.setSendTime(LocalDateTime.now());
        inboxMapper.insert(inbox);
    }

    /**
     * Entity → MessageSendVO
     */
    private MessageSendVO toSendVO(RecordEntity entity, List<String> channels) {
        MessageSendVO vo = new MessageSendVO();
        vo.setRecordId(entity.getId());
        vo.setTemplateId(entity.getTemplateId());
        vo.setTemplateCode(entity.getTemplateCode());
        vo.setReceiverId(entity.getReceiverId());
        vo.setReceiverName(entity.getReceiverName());
        vo.setChannels(channels);
        vo.setSendStatus(entity.getSendStatus());
        vo.setSendMsg(entity.getSendMsg());
        vo.setSendTime(entity.getSendTime());
        return vo;
    }

    /**
     * Entity → RecordVO
     */
    private RecordVO toRecordVO(RecordEntity entity) {
        RecordVO vo = new RecordVO();
        BeanUtil.copyProperties(entity, vo);
        // 敏感字段脱敏：手机号
        if (vo.getReceiverPhone() != null && vo.getReceiverPhone().length() > 7) {
            vo.setReceiverPhone(vo.getReceiverPhone().substring(0, 3) + "****" + vo.getReceiverPhone().substring(7));
        }
        return vo;
    }
}
