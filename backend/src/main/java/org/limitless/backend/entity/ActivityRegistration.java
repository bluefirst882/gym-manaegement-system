package org.limitless.backend.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 活动报名表 activity_registration
 */
@Data
public class ActivityRegistration {
    private Long id;
    private String registrationNo;
    private Integer activityId;
    private Integer userId;
    private String activityType;
    private String remark;
    private String auditStatus;
    private String auditComment;
    private LocalDateTime auditTime;
    private LocalDateTime createdAt;

    /** 活动标题（非数据库字段） */
    private String activityTitle;
    /** 客户姓名（非数据库字段） */
    private String customerName;
    /** 客户电话（非数据库字段） */
    private String customerPhone;
}
