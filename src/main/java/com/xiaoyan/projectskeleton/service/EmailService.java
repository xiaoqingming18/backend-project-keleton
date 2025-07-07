package com.xiaoyan.projectskeleton.service;

import java.io.File;
import java.util.List;

/**
 * 邮件服务接口
 */
public interface EmailService {
    
    /**
     * 发送纯文本邮件
     *
     * @param from    发送人邮箱
     * @param to      接收人邮箱，多个邮箱以逗号分隔
     * @param subject 邮件主题
     * @param content 邮件内容
     */
    void sendTextEmail(String from, String to, String subject, String content);
    
    /**
     * 发送HTML邮件
     *
     * @param from    发送人邮箱
     * @param to      接收人邮箱，多个邮箱以逗号分隔
     * @param subject 邮件主题
     * @param content HTML内容
     */
    void sendHtmlEmail(String from, String to, String subject, String content);
    
    /**
     * 发送带附件的邮件
     *
     * @param from     发送人邮箱
     * @param to       接收人邮箱，多个邮箱以逗号分隔
     * @param subject  邮件主题
     * @param content  邮件内容
     * @param isHtml   是否为HTML格式
     * @param files    附件列表
     */
    void sendAttachmentEmail(String from, String to, String subject, String content, 
                            boolean isHtml, List<File> files);
    
    /**
     * 完整的邮件发送
     *
     * @param senderName   发送人名称
     * @param from         发送人邮箱
     * @param to           接收人邮箱，多个邮箱以逗号分隔
     * @param subject      邮件主题
     * @param content      邮件内容
     * @param isHtml       是否为HTML格式
     * @param cc           抄送人，多个邮箱以逗号分隔
     * @param bcc          密送人，多个邮箱以逗号分隔
     * @param files        附件列表
     */
    void sendEmail(String senderName, String from, String to, String subject, String content, 
                  boolean isHtml, String cc, String bcc, List<File> files);
} 