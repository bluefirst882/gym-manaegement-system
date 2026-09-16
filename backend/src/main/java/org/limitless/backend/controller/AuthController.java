package org.limitless.backend.controller;

import org.limitless.backend.common.Result;
import org.limitless.backend.dto.LoginRequest;
import org.limitless.backend.dto.LoginResponse;
import org.limitless.backend.service.AuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * 用户登录
     * POST /api/auth/login
     */
    @PostMapping("/login")
    public Result<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return Result.success("登录成功", response);
    }

    /**
     * 管理后台登录：仅系统管理员和场馆管理员可登录。
     * 普通客户仍使用 /api/auth/login 登录微信小程序。
     */
    @PostMapping("/admin-login")
    public Result<LoginResponse> adminLogin(@RequestBody LoginRequest request) {
        LoginResponse response = authService.loginAdmin(request);
        return Result.success("登录成功", response);
    }
}
