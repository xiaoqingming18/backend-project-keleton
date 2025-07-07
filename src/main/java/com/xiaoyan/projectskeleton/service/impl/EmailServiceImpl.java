package com.xiaoyan.projectskeleton.service.impl;

import com.xiaoyan.projectskeleton.common.exception.BusinessException;
import com.xiaoyan.projectskeleton.common.exception.EmailErrorCode;
import com.xiaoyan.projectskeleton.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * 邮件服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String defaultFrom;

    @Override
    public void sendTextEmail(String from, String to, String subject, String content) {
        // 使用默认发件人（如果未指定）
        from = StringUtils.isNotBlank(from) ? from : defaultFrom;
        // 发送纯文本邮件
        sendEmail(null, from, to, subject, content, false, null, null, null);
    }

    @Override
    public void sendHtmlEmail(String from, String to, String subject, String content) {
        // 使用默认发件人（如果未指定）
        from = StringUtils.isNotBlank(from) ? from : defaultFrom;
        // 发送HTML邮件
        sendEmail(null, from, to, subject, content, true, null, null, null);
    }

    @Override
    public void sendAttachmentEmail(String from, String to, String subject, String content, boolean isHtml, List<File> files) {
        // 使用默认发件人（如果未指定）
        from = StringUtils.isNotBlank(from) ? from : defaultFrom;
        // 发送带附件的邮件
        sendEmail(null, from, to, subject, content, isHtml, null, null, files);
    }

    @Override
    public void sendEmail(String senderName, String from, String to, String subject, String content, boolean isHtml, String cc, String bcc, List<File> files) {
        // 参数校验
        if (StringUtils.isBlank(to) || StringUtils.isBlank(subject) || StringUtils.isBlank(content)) {
            log.error("邮件发送失败：必要参数缺失，收件人={}, 主题={}", to, subject);
            throw new BusinessException(EmailErrorCode.EMAIL_PARAMS_INCOMPLETE);
        }

        // 使用默认发件人（如果未指定）
        from = StringUtils.isNotBlank(from) ? from : defaultFrom;
        
        try {
            // 创建邮件消息
            MimeMessage message = javaMailSender.createMimeMessage();
            // 第二个参数为true表示支持附件
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            // 设置发件人
            if (StringUtils.isNotBlank(senderName)) {
                helper.setFrom(new InternetAddress(from, senderName, "UTF-8"));
            } else {
                helper.setFrom(from);
            }
            
            // 设置收件人（多个收件人以逗号分隔）
            helper.setTo(to.split(","));
            
            // 设置抄送人
            if (StringUtils.isNotBlank(cc)) {
                helper.setCc(cc.split(","));
            }
            
            // 设置密送人
            if (StringUtils.isNotBlank(bcc)) {
                helper.setBcc(bcc.split(","));
            }
            
            // 设置主题
            helper.setSubject(subject);
            
            // 设置内容，第二个参数表示是否为HTML格式
            helper.setText(content, isHtml);
            
            // 添加附件
            if (files != null && !files.isEmpty()) {
                for (File file : files) {
                    if (file.exists() && file.isFile()) {
                        FileSystemResource resource = new FileSystemResource(file);
                        helper.addAttachment(file.getName(), resource);
                    } else {
                        log.warn("邮件附件不存在或非文件：{}", file.getAbsolutePath());
                    }
                }
            }
            
            // 发送邮件
            try {
                log.info("开始发送邮件到：{}, 主题：{}", to, subject);
                javaMailSender.send(message);
                log.info("邮件发送成功：收件人={}, 主题={}", to, subject);
            } catch (Exception e) {
                // 详细记录邮件发送错误
                log.error("邮件发送失败 - 收件人: {}, 错误: {}", to, e.getMessage());
                
                if (e.getCause() != null) {
                    log.error("邮件发送失败的根本原因: {}", e.getCause().getMessage());
                }
                
                throw new BusinessException(EmailErrorCode.EMAIL_SEND_FAILED, "邮件发送失败：" + e.getMessage());
            }
            
        } catch (MessagingException e) {
            log.error("邮件发送异常：", e);
            throw new BusinessException(EmailErrorCode.EMAIL_SEND_FAILED, "邮件发送异常：" + e.getMessage());
        } catch (UnsupportedEncodingException e) {
            log.error("编码异常：", e);
            throw new BusinessException(EmailErrorCode.EMAIL_SEND_FAILED, "邮件编码异常：" + e.getMessage());
        } catch (Exception e) {
            log.error("邮件发送失败：", e);
            throw new BusinessException(EmailErrorCode.EMAIL_SEND_FAILED, "邮件发送失败：" + e.getMessage());
        }
    }
} 