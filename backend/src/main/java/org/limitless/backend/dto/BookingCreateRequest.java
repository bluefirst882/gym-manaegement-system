package org.limitless.backend.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalTime;

/**
 * 预约创建请求
 */
@Data
public class BookingCreateRequest {
    private Integer userId;
    private Integer venueId;
    private String bookingDate;
    private String startTime;
    private String endTime;
    private String remark;
}
