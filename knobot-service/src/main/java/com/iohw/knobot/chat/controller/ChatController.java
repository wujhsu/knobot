package com.iohw.knobot.chat.controller;

import com.iohw.knobot.chat.assistant.AssistantService;
import com.iohw.knobot.chat.entity.dto.ChatSessionDto;
import com.iohw.knobot.chat.entity.dto.ChatMessageDto;
import com.iohw.knobot.chat.request.ChatRequest;
import com.iohw.knobot.chat.request.command.CreateConversationCommand;
import com.iohw.knobot.chat.request.command.DeleteConversationCommand;
import com.iohw.knobot.chat.request.command.UpdateConversationTitleCommand;
import com.iohw.knobot.chat.vo.FileUploadVO;
import com.iohw.knobot.chat.service.ChatService;
import com.iohw.knobot.chat.service.SessionSideBarService;
import com.iohw.knobot.chat.vo.ChatSessionVO;
import com.iohw.knobot.response.Result;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.parser.TextDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.service.TokenStream;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static dev.langchain4j.data.document.loader.FileSystemDocumentLoader.loadDocument;

/**
 * @author: iohw
 * @date: 2025/4/12 19:37
 * @description:
 */
@Slf4j
@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {
    final EmbeddingModel embeddingModel;
    final EmbeddingStore<TextSegment> embeddingStore;
    final SessionSideBarService sessionSideBarService;
    final ChatService chatService;
    final AssistantService assistantService;

    private final String UPLOAD_PATH = "./documents";
    private static Map<String, String> filePathMap = new HashMap<>();

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamChat(@RequestParam("memoryId") String memoryId,  @RequestParam("input") String input) {
        SseEmitter emitter = new SseEmitter(-1L); // 无超时
        try {
            TokenStream tokenStream = assistantService.getRagAssistant(memoryId).chat(memoryId, input);
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


    @PostMapping("/upload")
    public Result<FileUploadVO> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            Path uploadPath = Paths.get(UPLOAD_PATH);
            if(!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            // 获取文件名
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            //保存文件
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            String fileId = String.valueOf(System.currentTimeMillis());
            FileUploadVO fileUploadVO = FileUploadVO.builder()
                    .fileId(fileId)
                    .fileName(fileName)
                    .filePath(filePath.toString())
                    .build();
            filePathMap.put(fileId, filePath.toString());
            return Result.success(fileUploadVO);
        } catch (IOException e) {
            return null;
        }

    }

    @GetMapping(value = "/{memoryId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter chat(@PathVariable String memoryId, ChatRequest request) {
        SseEmitter emitter = new SseEmitter(-1L); // 无超时
        String fileId = request.getFileId();
        //上传了附件
        if(!StringUtils.isEmpty(fileId)) {
            String filePath = filePathMap.get(fileId);
            loadFile2Store(filePath, memoryId);
        }

        try {
            TokenStream tokenStream = assistantService.getRagAssistant(memoryId).chat(memoryId, request.getUserMessage());
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

    private void loadFile2Store(String filePath, String memoryId) {
        Path path = Paths.get(filePath).toAbsolutePath().normalize();
        Document document = loadDocument(path.toString(), new TextDocumentParser());
        EmbeddingStoreIngestor embeddingStoreIngestor = EmbeddingStoreIngestor.builder()
                .embeddingModel(embeddingModel)
                .embeddingStore(embeddingStore)
                .documentSplitter(DocumentSplitters.recursive(300, 0))
                .documentTransformer(dc -> {
                    dc.metadata().put("memoryId", memoryId);
                    return dc;
                })
                .textSegmentTransformer(textSegment -> TextSegment.from(
                        textSegment.metadata().getString("file_name") + "\n" + textSegment.text(),
                        textSegment.metadata()
                ))
                .build();
        embeddingStoreIngestor.ingest(document);
    }


    @GetMapping("/conversation-history")
    public Result<List<ChatSessionDto>> queryChatConversationHistory(Long userId) {
        return sessionSideBarService.queryChatSessions(userId);
    }

    @PostMapping("/conversation-create")
    public Result<ChatSessionVO> createChatConversation(@RequestBody CreateConversationCommand command) {
        return sessionSideBarService.createChatSession(command);
    }

    @PostMapping("/conversation-delete")
    public Result<Void> deleteChatConversation(@RequestBody DeleteConversationCommand command) {
        return sessionSideBarService.deleteChatConversation(command);
    }

    @PostMapping("/conversation-title-update")
    public Result<Void> deleteChatConversationTitleUpdate(@RequestBody UpdateConversationTitleCommand command) {
        return sessionSideBarService.deleteChatConversationTitleUpdate(command);
    }

    @GetMapping("/messages")
    public Result<List<ChatMessageDto>> queryHistoryMessages(String memoryId) {
        return chatService.queryHistoryMessages(memoryId);
    }


}
