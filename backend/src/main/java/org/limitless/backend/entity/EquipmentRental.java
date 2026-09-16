package org.limitless.backend.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 设备租用表 equipment_rental
 */
@Data
public class EquipmentRental {
    private Integer id;
    private String rentalNo;
    private Integer equipmentId;
    private Integer quantity;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String borrower;
    private String contactPhone;
    private BigDecimal rentalPrice;
    private String status;
    private LocalDateTime actualReturnTime;
    private String remark;
    private LocalDateTime createdAt;

    /** 设备名称（非数据库字段） */
    private String equipmentName;
}
