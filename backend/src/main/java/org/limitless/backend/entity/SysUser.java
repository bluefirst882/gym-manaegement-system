package org.limitless.backend.entity;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 用户表 sys_user
 */
@Data
public class SysUser {
    private Integer id;
    private String username;
    private String password;
    private String realName;
    private String phone;
    private String gender;
    private LocalDate birthday;
    private Integer roleId;
    private Integer status;
    private String avatar;
    private LocalDateTime registeredAt;
    private LocalDateTime lastLoginAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /** 关联角色名称（非数据库字段） */
    private String roleName;
}
