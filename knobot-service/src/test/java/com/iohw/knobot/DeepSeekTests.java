package com.iohw.knobot;

import com.iohw.knobot.chat.assistant.IAssistant.base.Assistant;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import org.junit.jupiter.api.Test;

/**
 * @author: iohw
 * @date: 2025/4/27 23:00
 * @description:
 */
public class DeepSeekTests {
    @Test
    public void test() {
        ChatLanguageModel model = OpenAiChatModel.builder()
                .apiKey("sk-6688023f6f3643d59c9259c2ba576c53")
                .modelName("deepseek-reasoner")
                .baseUrl("https://api.deepseek.com")
                .build();
        Assistant assistant = AiServices.builder(Assistant.class)
                .chatLanguageModel(model)
                .build();
        System.out.println(assistant.chat("你好"));
    }
}
