package org.limitless.backend.dto;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 场地分页查询请求
 */
@Data
public class VenuePageRequest {
    private int pageNumber = 1;
    private int pageSize = 10;
    private String venueName;
    private String venueAddress;
    private Integer categoryId;
    private String status;
}
