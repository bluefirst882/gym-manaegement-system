package org.limitless.backend.service;

import org.junit.jupiter.api.*;
import org.limitless.backend.common.BusinessException;
import org.limitless.backend.entity.Equipment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class EquipmentServiceTest {

    @Autowired
    private EquipmentService equipmentService;

    @Test
    @Order(1)
    void create_shouldInsertEquipment() {
        System.out.println("\n========== EquipmentServiceTest.create ==========");
        Equipment equipment = new Equipment();
        equipment.setName("测试设备_" + System.currentTimeMillis());
        equipment.setCategoryId(1);
        equipment.setBrand("测试品牌");
        equipment.setModel("测试型号");
        equipment.setSerialNumber("SN_TEST_" + System.currentTimeMillis());
        equipment.setLocation("器材室A");
        equipment.setResponsiblePerson("管理员");
        equipment.setQuantity(10);
        equipment.setStatus("NORMAL");

        Integer id = equipmentService.create(equipment);
        System.out.println("created equipmentId: " + id);
        System.out.println("equipmentNo: " + equipment.getEquipmentNo());
        System.out.println("availableQuantity: " + equipment.getAvailableQuantity());

        Assertions.assertNotNull(id);
        Assertions.assertEquals(10, equipment.getAvailableQuantity().intValue());
    }

    @Test
    @Order(2)
    void delete_shouldRemoveEquipment() {
        System.out.println("\n========== EquipmentServiceTest.delete ==========");
        // 先创建一条
        Equipment eq = new Equipment();
        eq.setName("待删除设备");
        eq.setCategoryId(1);
        eq.setBrand("B");
        eq.setModel("M");
        eq.setSerialNumber("SN_DEL_" + System.currentTimeMillis());
        eq.setLocation("R1");
        eq.setResponsiblePerson("Admin");
        eq.setQuantity(1);
        Integer id = equipmentService.create(eq);
        System.out.println("创建待删除设备 ID: " + id);

        equipmentService.delete(id);
        System.out.println("设备 " + id + " 已删除");
    }
}
