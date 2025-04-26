package com.iohw.knobot.chat.request.command;

import lombok.Data;

/**
 * @author: iohw
 * @date: 2025/4/22 23:34
 * @description:
 */
@Data
public class UpdateConversationTitleCommand {
    private String memoryId;
    private String newTitle;
}
