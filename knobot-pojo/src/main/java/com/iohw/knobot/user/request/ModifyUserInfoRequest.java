package com.iohw.knobot.user.request;

import lombok.Data;

/**
 * @author: iohw
 * @date: 2025/5/4 22:20
 * @description:
 */
@Data
public class ModifyUserInfoRequest {
    private Long userId;
    private String password;
    private String nickname;
    private String avatarUrl;
}
