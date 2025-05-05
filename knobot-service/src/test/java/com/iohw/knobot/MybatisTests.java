package com.iohw.knobot;

import com.iohw.knobot.chat.model.ChatSessionDO;
import com.iohw.knobot.chat.mapper.ChatMessageMapper;
import com.iohw.knobot.chat.mapper.ChatSessionMapper;
import com.iohw.knobot.libary.mapper.KnowledgeLibMapper;
import com.iohw.knobot.library.model.KnowledgeLibDO;
import com.iohw.knobot.utils.IdGeneratorUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

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
    @Autowired
    private KnowledgeLibMapper knowledgeLibMapper;
    @Test
    public void test() {
        List<ChatSessionDO> chatSessionDOS = sessionMapper.selectByUserId(2l, 0);
        System.out.println("chatSessionDOS = " + chatSessionDOS);
    }

    @Test
    public void knowledgeLibMapperTest() {
        KnowledgeLibDO knowledgeLibDO = new KnowledgeLibDO();
        knowledgeLibDO.setKnowledgeLibId(IdGeneratorUtil.generateLibId());
        knowledgeLibDO.setKnowledgeLibName("1");
        knowledgeLibDO.setKnowledgeLibDesc("2");
        knowledgeLibDO.setDocumentCount(0);

        knowledgeLibMapper.insert(knowledgeLibDO);
    }
}
