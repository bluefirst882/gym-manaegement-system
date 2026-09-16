package org.limitless.backend.dto;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 活动创建请求
 */
@Data
public class ActivityCreateRequest {
    private String title;
    private Integer categoryId;
    private String location;
    private String startTime;
    private String endTime;
    private String rules;
    private String description;
    private String contact;
    private String feeType;
    private BigDecimal feeAmount;
    private String coverImage;
    private String registrationMethod;
    private Integer maxParticipants;
    private String awards;
    private String status;
}
