package org.limitless.backend.dto;

import lombok.Data;
import java.time.LocalDate;

/**
 * 创建用户请求
 */
@Data
public class UserCreateRequest {
    private String username;
    private String phone;
    private String realName;
    private String gender;
    private LocalDate birthday;
    private Integer roleId;
    private Integer status;
}
