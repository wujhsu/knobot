package com.iohw.knobot.chat.controller;

import com.iohw.knobot.chat.assistant.IAssistant.EmailAssistant;
import com.iohw.knobot.chat.request.command.SubmitIssueCommand;
import com.iohw.knobot.chat.tool.SendEmailTool;
import com.iohw.knobot.response.Result;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.service.AiServices;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: iohw
 * @date: 2025/4/23 23:04
 * @description:
 */
@Slf4j
@RestController
@RequestMapping("/home")
public class HomeController {
    @Autowired
    private ChatLanguageModel model;

    @Autowired
    private SendEmailTool sendEmailTool;

    @PostMapping("/submit-issue")
    public Result<Void> submitIssue(@RequestBody SubmitIssueCommand issue) {
        EmailAssistant emailAssistant = AiServices.builder(EmailAssistant.class)
                .chatLanguageModel(model)
                .tools(sendEmailTool)
                .build();
        emailAssistant.sendEmailToAuthor(issue.getIssueDescription());
        return Result.success(null);
    }
}
