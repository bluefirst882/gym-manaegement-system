package org.limitless.backend.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 活动表 activity
 */
@Data
public class Activity {
    private Integer id;
    private String activityNo;
    private String title;
    private String coverImage;
    private String carouselImages;
    private Integer categoryId;
    private String location;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;
    private String registrationMethod;
    private Integer maxParticipants;
    private String awards;
    private String rules;
    private String description;
    private String contact;
    private String feeType;
    private BigDecimal feeAmount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /** 关联分类名称（非数据库字段） */
    private String categoryName;
}
