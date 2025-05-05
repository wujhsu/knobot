package com.iohw.knobot.chat.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: iohw
 * @date: 2025/4/14 22:05
 * @description:
 */
@AllArgsConstructor
public enum ChatSessionEnum {
    NORMAL(0, "正常"),
    DELETED(1, "已删除");
    @Getter
    private int status;
    private String desc;

}
