package org.limitless.backend.dto;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 活动更新请求
 */
@Data
public class ActivityUpdateRequest {
    private String title;
    private Integer categoryId;
    private String location;
    private String rules;
    private String description;
    private String contact;
    private String feeType;
    private BigDecimal feeAmount;
    private String coverImage;
    private String status;
    private Integer maxParticipants;
    private String awards;
}
