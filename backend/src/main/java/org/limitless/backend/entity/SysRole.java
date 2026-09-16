package org.limitless.backend.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 角色表 sys_role
 */
@Data
public class SysRole {
    private Integer id;
    private String roleCode;
    private String roleName;
    private String roleDesc;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
