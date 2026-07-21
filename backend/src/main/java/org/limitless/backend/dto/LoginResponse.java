package org.limitless.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 登录响应
 */
@Data
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private UserInfo userInfo;

    @Data
    @AllArgsConstructor
    public static class UserInfo {
        private Integer id;
        private String username;
        private String realName;
        private String phone;
        private String gender;
        private Integer roleId;
        private String roleName;
    }
}
