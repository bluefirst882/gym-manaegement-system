package org.limitless.backend.dto;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 创建场地请求
 */
@Data
public class VenueCreateRequest {
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
    private String status;
    private String remark;
}
