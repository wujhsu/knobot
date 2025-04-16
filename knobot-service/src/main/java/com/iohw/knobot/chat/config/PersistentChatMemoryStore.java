package com.iohw.knobot.chat.config;

import com.alibaba.fastjson.JSONObject;
import com.iohw.knobot.chat.entity.ChatMessageDO;
import com.iohw.knobot.chat.mapper.ChatMessageMapper;
import com.iohw.knobot.chat.mapper.ChatSessionMapper;
import dev.langchain4j.data.message.*;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author: iohw
 * @date: 2025/4/13 10:35
 * @description:
 */
@Component
public class PersistentChatMemoryStore implements ChatMemoryStore {
    @Autowired
    private ChatSessionMapper chatSessionMapper;
    @Autowired
    private ChatMessageMapper chatMessageMapper;

    final Map<String, String> map = new HashMap<>();


    @Override
    public List<ChatMessage> getMessages(Object o) {
//        String json = map.get(o.toString());
        String memoryId = (String) o;
        List<ChatMessage> messages = new ArrayList<>();
        if(memoryId.equals("default")) return messages;
        List<ChatMessageDO> chatMessageDOS = chatMessageMapper.selectByMemoryId(memoryId);
        for (ChatMessageDO chatMessageDO : chatMessageDOS) {
            String role = chatMessageDO.getRole();
            String content = chatMessageDO.getContent();
            ChatMessage message = switch (role.toLowerCase()) {
                case "system" -> SystemMessage.from(content);
                case "user" -> UserMessage.from(content);
                case "assistant" -> AiMessage.from(content);
                case "tool" -> parseToolMessage(content);
                //case "custom" -> new CustomMessage(content);
                default -> throw new RuntimeException("不存在该角色: " + role.toLowerCase());
            };
            messages.add(message);
        }
        return messages;
    }

    @Override
    public void updateMessages(Object o, List<ChatMessage> list) {
//        String json = ChatMessageSerializer.messagesToJson(list);
//        map.put(o.toString(), json);
        // todo 优化：增量更新 or ...
        String memoryId = o.toString();
        deleteMessages(memoryId);
        List<ChatMessageDO> messageList = new ArrayList<>();
        for (ChatMessage chatMessage : list) {
            String role = getRoleFromMessage(chatMessage);
            String content = getContentMessage(chatMessage);
            ChatMessageDO chatHistoryDO = ChatMessageDO.builder()
                    .role(role)
                    .content(content)
                    .memoryId(memoryId)
                    .messageId(UUID.randomUUID().toString())
                    .build();
            messageList.add(chatHistoryDO);
        }
        chatMessageMapper.batchInsert(messageList);
    }

    @Override
    public void deleteMessages(Object o) {
        String memoryId = o.toString();
        chatMessageMapper.deleteByMemoryId(memoryId);
    }

    private String getRoleFromMessage(ChatMessage message) {
        if (message instanceof SystemMessage) {
            return "system";
        } else if (message instanceof UserMessage) {
            return "user";
        } else if (message instanceof AiMessage) {
            return "assistant";
        }else if (message instanceof ToolExecutionResultMessage) {
            return "tool";
        } else if (message instanceof CustomMessage) {
            return "custom";
        }
        throw new IllegalArgumentException("Unknown message type: " + message.getClass().getName());
    }

    private String getContentMessage(ChatMessage message) {
        if (message instanceof SystemMessage) {
            return ((SystemMessage) message).text();
        } else if (message instanceof UserMessage) {
            return ((UserMessage) message).singleText();
        } else if (message instanceof AiMessage) {
            return ((AiMessage) message).text();
        }else if (message instanceof ToolExecutionResultMessage) {
            // 工具执行结果需要特殊处理
            ToolExecutionResultMessage toolMsg = (ToolExecutionResultMessage) message;
            return String.format("{id: %s, tool_name: %s, execution_result: %s}",
                    toolMsg.id(), toolMsg.toolName(), toolMsg.text());
        } else if (message instanceof CustomMessage) {
            // 自定义消息可能需要JSON序列化
            return ((CustomMessage) message).toString();
        }
        throw new IllegalArgumentException("Unknown message type: " + message.getClass().getName());
    }

    private ToolExecutionResultMessage parseToolMessage(String content) {
        // 简单实现 - 实际应根据存储格式调整
        try {
            JSONObject json = new JSONObject(Boolean.parseBoolean(content.replace("{", "{\"").replace(":", "\":\"")));
            return new ToolExecutionResultMessage(
                    json.getString("message_id"),
                    json.getString("tool_name"),
                    json.getString("execution_result")
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }}
