package org.limitless.backend.service;

import org.junit.jupiter.api.*;
import org.limitless.backend.entity.EquipmentMaintenance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class EquipmentMaintenanceServiceTest {

    @Autowired
    private EquipmentMaintenanceService maintenanceService;

    @Test
    @Order(1)
    void create_shouldInsertMaintenance() {
        System.out.println("\n========== EquipmentMaintenanceServiceTest.create ==========");
        EquipmentMaintenance maintenance = new EquipmentMaintenance();
        maintenance.setTitle("安全带定期检测");
        maintenance.setEquipmentId(2);
        maintenance.setMaintenanceTime(java.time.LocalDateTime.parse("2026-08-01T09:00:00"));
        maintenance.setContent("对所有安全带进行安全性检测");
        maintenance.setPersonnel("设备维护部");
        maintenance.setEquipmentCondition("NORMAL");

        Integer id = maintenanceService.create(maintenance);
        System.out.println("created maintenanceId: " + id);
        System.out.println("maintenanceNo: " + maintenance.getMaintenanceNo());

        Assertions.assertNotNull(id);
    }

    @Test
    @Order(2)
    void update_shouldModifyContent() {
        System.out.println("\n========== EquipmentMaintenanceServiceTest.update ==========");
        EquipmentMaintenance update = new EquipmentMaintenance();
        update.setContent("更新后的检测内容");
        update.setPersonnel("高级检测员");

        maintenanceService.update(1, update);
        System.out.println("维护记录 1 更新成功");
    }

    @Test
    @Order(3)
    void delete_shouldRemoveRecord() {
        System.out.println("\n========== EquipmentMaintenanceServiceTest.delete ==========");
        maintenanceService.delete(2);
        System.out.println("维护记录 2 已删除");
    }
}
