package org.limitless.backend.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.limitless.backend.common.PageRequest;

/**
 * 用户分页查询请求
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserPageRequest extends PageRequest {
    private String username;
    private String realName;
    private String phone;
    private Integer roleId;
}
