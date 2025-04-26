package com.iohw.knobot.chat.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "mail")
public class EmailConfig {
    private String fromAddress;
    private String toAddress;
    private String template = """
            <div style="font-family: Arial, sans-serif; line-height: 1.6;">
                <p><strong>问题反馈时间：</strong>%s</p>

                <p><strong>问题描述：</strong></p>
                <p>%s</p>

                <hr style="border: none; border-top: 1px solid #ccc; margin: 20px 0;">
                <p style="color: #666; font-size: 12px;">此邮件由系统自动发送，请勿直接回复。</p>
            </div>
            """;
}