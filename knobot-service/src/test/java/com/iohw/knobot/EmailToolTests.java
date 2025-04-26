package com.iohw.knobot;

import com.iohw.knobot.chat.assistant.IAssistant.EmailAssistant;
import com.iohw.knobot.chat.assistant.IAssistant.base.Assistant;
import com.iohw.knobot.chat.request.command.SubmitIssueCommand;
import com.iohw.knobot.chat.tool.SendEmailTool;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.service.AiServices;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

/**
 * @author: iohw
 * @date: 2025/4/23 22:44
 * @description:
 */
@SpringBootTest(classes = KnobotServiceApplication.class)
public class EmailToolTests {
    @Autowired
    private ChatLanguageModel model;
    @Autowired
    private SendEmailTool sendEmailTool;
    @Autowired
    private JavaMailSenderImpl javaMailSender;
    @Test
    public void test() {
        EmailAssistant emailAssistant = AiServices.builder(EmailAssistant.class)
                .chatLanguageModel(model)
                .tools(sendEmailTool)
                .build();
        emailAssistant.chat("有一个功能bug，点击发送不能够正常发送，请你发送给作者");
    }

    @Test
    public void test1() {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setSubject("测试");
            helper.setText("123");
            helper.setTo("2023321332@qq.com");
            helper.setFrom("3979892804@qq.com");
            javaMailSender.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void test3() {
        EmailAssistant emailAssistant = AiServices.builder(EmailAssistant.class)
                .chatLanguageModel(model)
                .tools(sendEmailTool)
                .build();
        SubmitIssueCommand command  = SubmitIssueCommand.builder()
                .title("字符串乘法计算错误，补零位置不正确 ")
                .issueDescription(
                                """
                                - 修正 `num1.charAt(j)` 的使用。 \s
                                 - 调整补零逻辑，确保在数字反转后再补零。 \s
                                """
                )
                .build();
        System.out.println(emailAssistant.sendEmailToAuthor(command));
    }
}
