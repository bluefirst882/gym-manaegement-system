package org.limitless.backend.dto;

import lombok.Data;
import java.time.LocalDate;

/**
 * 更新用户请求
 */
@Data
public class UserUpdateRequest {
    private String realName;
    private String phone;
    private String gender;
    private LocalDate birthday;
    private Integer roleId;
    private Integer status;
}
