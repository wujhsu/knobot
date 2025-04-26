package com.iohw.knobot.chat.controller;

import com.iohw.knobot.chat.assistant.AssistantService;
import com.iohw.knobot.chat.assistant.IAssistant.StreamingAssistant;
import com.iohw.knobot.chat.assistant.IAssistant.WebSearchAssistant;
import com.iohw.knobot.chat.entity.dto.ChatSessionDto;
import com.iohw.knobot.chat.entity.dto.ChatMessageDto;
import com.iohw.knobot.chat.request.ChangeKnowledgeLibCommand;
import com.iohw.knobot.chat.request.ChatRequest;
import com.iohw.knobot.chat.request.command.CreateConversationCommand;
import com.iohw.knobot.chat.request.command.DeleteConversationCommand;
import com.iohw.knobot.chat.request.command.UpdateConversationTitleCommand;
import com.iohw.knobot.chat.vo.FileUploadVO;
import com.iohw.knobot.chat.service.ChatService;
import com.iohw.knobot.chat.service.SessionSideBarService;
import com.iohw.knobot.chat.vo.ChatSessionVO;
import com.iohw.knobot.common.dto.FileUploadDto;
import com.iohw.knobot.response.Result;
import com.iohw.knobot.upload.FileUploadFactory;
import com.iohw.knobot.upload.UploadFileStrategy;
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
    final WebSearchAssistant webSearchAssistant;
    final FileUploadFactory fileUploadFactory;

    private static Map<String, String> filePathMap = new HashMap<>();


    @PostMapping("/upload")
    public Result<FileUploadVO> uploadFile(@RequestParam("file") MultipartFile file) {
        UploadFileStrategy uploadStrategy = fileUploadFactory.getUploadStrategy();
        FileUploadDto uploadDto = uploadStrategy.upload(file, "/documents");

        FileUploadVO fileUploadVO = FileUploadVO.builder()
                .fileId(uploadDto.getFileId())
                .fileName(uploadDto.getFileName())
                .filePath(uploadDto.getFilePath())
                .build();
        filePathMap.put(uploadDto.getFileId(), uploadDto.getFilePath());
        return Result.success(fileUploadVO);
    }

    @GetMapping(value = "/{memoryId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter chat(@PathVariable String memoryId, ChatRequest request) {
        SseEmitter emitter = new SseEmitter(-1L); // 无超时
        String fileId = request.getFileId();
        //上传了附件
        if(!StringUtils.isEmpty(fileId)) {
            String filePath = filePathMap.get(fileId);
            loadFile2Store(filePath, memoryId, request.getKnowledgeLibId());
        }

        try {
            StreamingAssistant assistant = assistantService.getRagAssistant(memoryId, request.getKnowledgeLibId());
            // 开启联网搜索
            if(request.getIsWebSearchRequest()) {
                assistant = webSearchAssistant;
            }
            TokenStream tokenStream = assistant.chat(memoryId, request.getUserMessage());
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

    private void loadFile2Store(String filePath, String memoryId, String knowledgeLibId) {
        Path path = Paths.get(filePath).toAbsolutePath().normalize();
        Document document = loadDocument(path.toString(), new TextDocumentParser());
        EmbeddingStoreIngestor embeddingStoreIngestor = EmbeddingStoreIngestor.builder()
                .embeddingModel(embeddingModel)
                .embeddingStore(embeddingStore)
                .documentSplitter(DocumentSplitters.recursive(300, 20))
                .documentTransformer(dc -> {
                    dc.metadata().put("memoryId", memoryId);
                    if(knowledgeLibId != null) {
                        dc.metadata().put("knowledgeLibId", knowledgeLibId);
                    }
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
        return sessionSideBarService.queryChatConversation(userId);
    }

    @PostMapping("/conversation-create")
    public Result<ChatSessionVO> createChatConversation(@RequestBody CreateConversationCommand command) {
        return sessionSideBarService.createChatConversation(command);
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

    @PostMapping("/changeKnowledgeLib")
    public Result<Void> changeKnowledgeLib(ChangeKnowledgeLibCommand command) {
        //切换知识库 --> 更改知识库过滤条件

        return Result.success(null);
    }
}
