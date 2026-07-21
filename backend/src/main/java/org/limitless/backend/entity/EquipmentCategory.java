package org.limitless.backend.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 设备分类表 equipment_category
 */
@Data
public class EquipmentCategory {
    private Integer id;
    private String categoryCode;
    private String categoryName;
    private String categoryDesc;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
