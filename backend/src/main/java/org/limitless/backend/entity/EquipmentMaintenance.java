package org.limitless.backend.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 设备维护表 equipment_maintenance
 */
@Data
public class EquipmentMaintenance {
    private Integer id;
    private String maintenanceNo;
    private String title;
    private Integer equipmentId;
    private LocalDateTime maintenanceTime;
    private String content;
    private String personnel;
    private String equipmentCondition;
    private String images;
    private String remark;
    private LocalDateTime createdAt;

    /** 设备名称（非数据库字段） */
    private String equipmentName;
}
