package org.limitless.backend.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 设备表 equipment
 */
@Data
public class Equipment {
    private Integer id;
    private String equipmentNo;
    private String name;
    private Integer categoryId;
    private String brand;
    private String model;
    private String serialNumber;
    private String location;
    private String responsiblePerson;
    private Integer quantity;
    private Integer availableQuantity;
    private String status;
    private String coverImage;
    private String detailImages;
    private String remark;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /** 分类名称（非数据库字段） */
    private String categoryName;
}
