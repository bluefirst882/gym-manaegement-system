package org.limitless.backend.dto;

import lombok.Data;

/**
 * 预约分页查询请求
 */
@Data
public class BookingPageRequest {
    private int pageNumber = 1;
    private int pageSize = 10;
    private String bookingNo;
    private Integer userId;
    private Integer venueId;
    private String status;
    private String paymentStatus;
    private String bookingDateStart;
    private String bookingDateEnd;
}
