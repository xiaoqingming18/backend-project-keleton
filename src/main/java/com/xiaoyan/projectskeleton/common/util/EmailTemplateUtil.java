package com.xiaoyan.projectskeleton.common.util;

import java.util.Map;
import org.apache.commons.lang3.StringUtils;

/**
 * 邮件模板工具类
 * 提供常用HTML邮件模板
 */
public class EmailTemplateUtil {
    
    /**
     * 生成一个简单的HTML邮件模板
     * 
     * @param title    标题
     * @param content  正文内容
     * @param footer   页脚
     * @return HTML字符串
     */
    public static String getSimpleTemplate(String title, String content, String footer) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>")
            .append("<html>")
            .append("<head>")
            .append("<meta charset=\"UTF-8\">")
            .append("<title>").append(title).append("</title>")
            .append("<style>")
            .append("body { font-family: Arial, sans-serif; margin: 0; padding: 20px; color: #333; }")
            .append(".container { max-width: 600px; margin: 0 auto; background-color: #fff; border: 1px solid #e4e4e4; border-radius: 5px; padding: 20px; }")
            .append(".header { text-align: center; padding: 10px; border-bottom: 1px solid #e4e4e4; margin-bottom: 20px; }")
            .append(".content { line-height: 1.6; }")
            .append(".footer { text-align: center; padding-top: 20px; border-top: 1px solid #e4e4e4; margin-top: 20px; font-size: 12px; color: #777; }")
            .append("</style>")
            .append("</head>")
            .append("<body>")
            .append("<div class=\"container\">")
            .append("<div class=\"header\"><h2>").append(title).append("</h2></div>")
            .append("<div class=\"content\">").append(content).append("</div>")
            .append("<div class=\"footer\">").append(footer).append("</div>")
            .append("</div>")
            .append("</body>")
            .append("</html>");
        return html.toString();
    }
    
    /**
     * 生成通知邮件模板
     * 
     * @param title    通知标题
     * @param message  通知内容
     * @param footer   页脚
     * @return HTML字符串
     */
    public static String getNotificationTemplate(String title, String message, String footer) {
        StringBuilder content = new StringBuilder();
        content.append("<p>尊敬的用户：</p>")
               .append("<p>").append(message).append("</p>")
               .append("<p>此致，<br/>系统团队</p>");
        
        return getSimpleTemplate(title, content.toString(), footer);
    }
    
    /**
     * 生成验证码邮件模板
     * 
     * @param code     验证码
     * @param validTime 有效时间描述，如"10分钟"
     * @param footer   页脚
     * @return HTML字符串
     */
    public static String getVerificationCodeTemplate(String code, String validTime, String footer) {
        StringBuilder content = new StringBuilder();
        content.append("<p>尊敬的用户：</p>")
               .append("<p>您的验证码为：</p>")
               .append("<p style=\"font-size: 24px; font-weight: bold; color: #333; text-align: center; padding: 15px; background-color: #f7f7f7; border-radius: 5px; letter-spacing: 5px;\">")
               .append(code)
               .append("</p>")
               .append("<p>验证码有效期为").append(validTime).append("，请勿将验证码泄露给他人。</p>")
               .append("<p>如非本人操作，请忽略此邮件。</p>");
        
        return getSimpleTemplate("验证码", content.toString(), footer);
    }
    
    /**
     * 生成带按钮的HTML邮件模板
     * 
     * @param title      标题
     * @param content    正文内容
     * @param buttonText 按钮文字
     * @param buttonUrl  按钮链接
     * @param footer     页脚
     * @return HTML字符串
     */
    public static String getButtonTemplate(String title, String content, String buttonText, String buttonUrl, String footer) {
        StringBuilder html = new StringBuilder(content);
        html.append("<div style=\"text-align: center; margin: 30px 0;\">")
            .append("<a href=\"").append(buttonUrl).append("\" style=\"display: inline-block; padding: 10px 20px; background-color: #007bff; color: #fff; text-decoration: none; border-radius: 4px; font-weight: bold;\">")
            .append(buttonText)
            .append("</a>")
            .append("</div>");
        
        return getSimpleTemplate(title, html.toString(), footer);
    }
    
    /**
     * 根据模板和参数生成HTML内容
     * 
     * @param template  HTML模板，参数使用 ${paramName} 表示
     * @param params    参数映射
     * @return 替换参数后的HTML内容
     */
    public static String processTemplate(String template, Map<String, String> params) {
        String result = template;
        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                result = result.replace("${" + entry.getKey() + "}", entry.getValue());
            }
        }
        return result;
    }
    
    /**
     * 生成用户封禁通知邮件模板
     * 
     * @param username  用户名
     * @param reason    封禁原因
     * @param contactEmail 联系邮箱（可为空）
     * @param footer    页脚
     * @return HTML字符串
     */
    public static String getBanNotificationTemplate(String username, String reason, String contactEmail, String footer) {
        StringBuilder content = new StringBuilder();
        content.append("<p>尊敬的 <strong>").append(username).append("</strong>：</p>")
               .append("<p>很遗憾地通知您，您的账号已被封禁。</p>")
               .append("<div style=\"margin: 20px 0; padding: 15px; background-color: #f8f9fa; border-left: 4px solid #dc3545; border-radius: 3px;\">")
               .append("<p><strong>封禁原因：</strong></p>");
        
        if (StringUtils.isNotBlank(reason)) {
            content.append("<p>").append(reason).append("</p>");
        } else {
            content.append("<p>违反了平台使用规定。</p>");
        }
        
        content.append("</div>")
               .append("<p>如果您对此有任何疑问，或认为这是一个错误，请联系我们的客服团队。</p>");
               
        if (StringUtils.isNotBlank(contactEmail)) {
            content.append("<p>联系邮箱：<a href=\"mailto:").append(contactEmail).append("\">").append(contactEmail).append("</a></p>");
        }
        
        content.append("<p>此致，<br/>系统管理团队</p>");
        
        return getSimpleTemplate("账号封禁通知", content.toString(), footer);
    }
} 