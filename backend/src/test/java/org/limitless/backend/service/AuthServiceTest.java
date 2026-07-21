package org.limitless.backend.service;

import org.junit.jupiter.api.*;
import org.limitless.backend.common.BusinessException;
import org.limitless.backend.dto.LoginRequest;
import org.limitless.backend.dto.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Test
    @Order(1)
    void login_admin_shouldSucceed() {
        System.out.println("\n========== AuthServiceTest.login_admin ==========");
        LoginRequest request = new LoginRequest();
        request.setUsername("admin");
        request.setPassword("123456");

        LoginResponse response = authService.login(request);

        System.out.println("token: " + response.getToken());
        System.out.println("userInfo.id: " + response.getUserInfo().getId());
        System.out.println("userInfo.username: " + response.getUserInfo().getUsername());
        System.out.println("userInfo.realName: " + response.getUserInfo().getRealName());
        System.out.println("userInfo.phone: " + response.getUserInfo().getPhone());
        System.out.println("userInfo.roleId: " + response.getUserInfo().getRoleId());
        System.out.println("userInfo.roleName: " + response.getUserInfo().getRoleName());

        Assertions.assertNotNull(response.getToken());
        Assertions.assertEquals("admin", response.getUserInfo().getUsername());
        Assertions.assertEquals("系统管理员", response.getUserInfo().getRealName());
        Assertions.assertEquals("系统管理员", response.getUserInfo().getRoleName());
    }

    @Test
    @Order(2)
    void login_customer_shouldSucceed() {
        System.out.println("\n========== AuthServiceTest.login_customer ==========");
        LoginRequest request = new LoginRequest();
        request.setUsername("zhangxiao");
        request.setPassword("123456");

        LoginResponse response = authService.login(request);

        System.out.println("token: " + response.getToken());
        System.out.println("userInfo.id: " + response.getUserInfo().getId());
        System.out.println("userInfo.username: " + response.getUserInfo().getUsername());
        System.out.println("userInfo.realName: " + response.getUserInfo().getRealName());
        System.out.println("userInfo.roleId: " + response.getUserInfo().getRoleId());
        System.out.println("userInfo.roleName: " + response.getUserInfo().getRoleName());

        Assertions.assertEquals("zhangxiao", response.getUserInfo().getUsername());
        Assertions.assertEquals("普通客户", response.getUserInfo().getRoleName());
    }

    @Test
    @Order(3)
    void login_withWrongPassword_shouldFail() {
        System.out.println("\n========== AuthServiceTest.login_withWrongPassword ==========");
        LoginRequest request = new LoginRequest();
        request.setUsername("admin");
        request.setPassword("wrong_password");

        Assertions.assertThrows(BusinessException.class, () -> authService.login(request));
        System.out.println("正确捕获异常：用户名或密码错误");
    }

    @Test
    @Order(4)
    void login_withNonExistentUser_shouldFail() {
        System.out.println("\n========== AuthServiceTest.login_withNonExistentUser ==========");
        LoginRequest request = new LoginRequest();
        request.setUsername("not_exists");
        request.setPassword("123456");

        Assertions.assertThrows(BusinessException.class, () -> authService.login(request));
        System.out.println("正确捕获异常：用户名或密码错误");
    }
}
