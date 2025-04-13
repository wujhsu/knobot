package com.iohw.knobot.chat.controller;

import com.iohw.knobot.chat.assistant.Assistant;
import com.iohw.knobot.chat.config.PersistentChatMemoryStore;
import com.iohw.knobot.chat.entity.dto.ChatMessageDto;
import com.iohw.knobot.chat.entity.dto.ChatSessionDto;
import com.iohw.knobot.chat.service.ChatService;
import com.iohw.knobot.chat.service.SessionSideBarService;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.TokenStream;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

/**
 * @author: iohw
 * @date: 2025/4/12 19:37
 * @description:
 */
@Slf4j
@RestController
@RequestMapping("/chat")
public class ChatController {
    @Autowired
    private SessionSideBarService sessionSideBarService;
    @Autowired
    private ChatService chatService;
    @Autowired
    @Qualifier(value = "basicAssistant")
    private Assistant assistant;


    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamChat(@RequestParam("memoryId") String memoryId,  @RequestParam("input") String input) {
        SseEmitter emitter = new SseEmitter(-1L); // 无超时
        try {
            TokenStream tokenStream = assistant.chat(memoryId, input);
            tokenStream
                    .onPartialResponse(token -> {
                        try {
                            emitter.send(SseEmitter.event()
                                    .data(token)
                                    .id(String.valueOf(System.currentTimeMillis()))
                                    .name("message"));
                        } catch (Exception e) {
                            emitter.completeWithError(e);
                        }
                    })
                    .onCompleteResponse(response -> {
                        try {
                            emitter.send(SseEmitter.event()
                                    .data("[DONE]")
                                    .id("done")
                                    .name("done"));
                            emitter.complete();
                        } catch (Exception e) {
                            emitter.completeWithError(e);
                        }
                    })
                    .onError(e -> {
                        emitter.completeWithError(e);
                    })
                    .start();
        } catch (Exception e) {
            emitter.completeWithError(e);
        }
        return emitter;
    }

    @GetMapping("/session")
    public List<ChatSessionDto> queryChatSessions(long userId) {
        return sessionSideBarService.queryChatSessions(userId);
    }

    @GetMapping("/messages")
    public List<ChatMessageDto> queryHistoryMessages(String memoryId) {
        return chatService.queryHistoryMessages(memoryId);
    }
}
