package com.iohw.knobot;

import com.iohw.knobot.chat.entity.ChatSessionDO;
import com.iohw.knobot.chat.mapper.ChatMessageMapper;
import com.iohw.knobot.chat.mapper.ChatSessionMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

/**
 * @author: iohw
 * @date: 2025/4/13 11:36
 * @description:
 */
@SpringBootTest(classes = KnobotServiceApplication.class)
public class MybatisTests {
    @Autowired
    private ChatSessionMapper sessionMapper;
    @Autowired
    private ChatMessageMapper messageMapper;
    @Test
    public void test() {
        ChatSessionDO chatSessionDO = ChatSessionDO.builder()
                        .userId(3l).memoryId(UUID.randomUUID().toString()).build();
        sessionMapper.insert(chatSessionDO);
    }
}
