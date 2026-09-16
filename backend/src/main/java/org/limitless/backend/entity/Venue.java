package org.limitless.backend.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 场地表 venue
 */
@Data
public class Venue {
    private Integer id;
    private String venueCode;
    private String venueName;
    private String venueAddress;
    private String contactPhone;
    private Integer categoryId;
    private String dimensions;
    private String material;
    private Integer capacity;
    private String facilities;
    private String feeType;
    private BigDecimal feeAmount;
    private String coverImage;
    private String detailImages;
    private String status;
    private String remark;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /** 关联分类名称（非数据库字段） */
    private String categoryName;
}
