package org.limitless.backend.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 场地预约表 booking
 */
@Data
public class Booking {
    private Long id;
    private String bookingNo;
    private Integer userId;
    private Integer venueId;
    private String venueName;
    private String venueAddress;
    private LocalDate bookingDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private BigDecimal durationHours;
    private String remark;
    private String status;
    private String source;
    private LocalDateTime checkinTime;
    private LocalDateTime checkoutTime;
    private Integer attendeeCount;
    private BigDecimal feeAmount;
    private String paymentStatus;
    private String paymentMethod;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /** 客户姓名（非数据库字段） */
    private String customerName;
    /** 客户电话（非数据库字段） */
    private String customerPhone;
}
