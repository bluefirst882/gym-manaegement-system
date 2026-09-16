package org.limitless.backend.dto;

import lombok.Data;

/**
 * 报名创建请求
 */
@Data
public class RegistrationCreateRequest {
    private Integer activityId;
    private Integer userId;
    private String activityType;
    private String remark;
}
