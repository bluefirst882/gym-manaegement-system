package org.limitless.backend.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 设备购买记录表 equipment_purchase
 */
@Data
public class EquipmentPurchase {
    private Integer id;
    private String purchaseNo;
    private Integer equipmentId;
    private String equipmentName;
    private String brand;
    private String model;
    private String serialNumber;
    private Integer quantity;
    private BigDecimal purchasePrice;
    private LocalDate purchaseDate;
    private String supplier;
    private String invoiceNo;
    private String remark;
    private LocalDateTime createdAt;
}
