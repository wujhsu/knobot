package com.iohw.knobot.user.controller;

import com.iohw.knobot.common.response.Result;
import com.iohw.knobot.user.model.dto.UserInfoDto;
import com.iohw.knobot.user.request.LoginRequest;
import com.iohw.knobot.user.request.ModifyUserInfoRequest;
import com.iohw.knobot.user.request.RegistryRequest;
import com.iohw.knobot.user.service.UserInfoService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: iohw
 * @date: 2025/5/4 22:00
 * @description: 用户控制器
 */
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserInfoService userInfoService;

    @PostMapping("/login")
    public Result<UserInfoDto> login(HttpServletRequest req, HttpServletResponse resp, LoginRequest request) {
        UserInfoDto userInfoDto = userInfoService.login(req, resp, request);
        return Result.success(userInfoDto);
    }

    @PostMapping("/logout")
    public Result<Void> logout(HttpServletRequest req, HttpServletResponse resp) {
        userInfoService.logout(resp, req);
        return Result.success("登出成功");
    }

    @PostMapping("/registry")
    public Result<Void> registry(RegistryRequest request) {
        userInfoService.createUser(request);
        return Result.success("注册成功");
    }

    @PostMapping("/modifyInfo")
    public Result<Void> modify(ModifyUserInfoRequest modifyUserInfoRequest) {
        if(userInfoService.updateUserInfo(modifyUserInfoRequest)) {
            return Result.success("更新成功");
        }
        return Result.error("更新失败");
    }
}