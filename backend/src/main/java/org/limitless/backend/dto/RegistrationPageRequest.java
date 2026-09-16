package org.limitless.backend.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.limitless.backend.common.PageRequest;

/**
 * 报名分页查询请求
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RegistrationPageRequest extends PageRequest {
    private Integer activityId;
    private String activityTitle;
    private Integer userId;
    private String auditStatus;
    private String regStartTime;
    private String regEndTime;
}
