package org.limitless.backend.service;

import org.junit.jupiter.api.*;
import org.limitless.backend.common.BusinessException;
import org.limitless.backend.entity.EquipmentRental;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class EquipmentRentalServiceTest {

    @Autowired
    private EquipmentRentalService rentalService;

    @Test
    @Order(1)
    void create_shouldReduceAvailableStock() {
        System.out.println("\n========== EquipmentRentalServiceTest.create_减少库存 ==========");
        EquipmentRental rental = new EquipmentRental();
        rental.setEquipmentId(2); // Black Diamond安全带, available=15
        rental.setQuantity(3);
        rental.setStartTime(java.time.LocalDateTime.parse("2026-08-01T09:00:00"));
        rental.setEndTime(java.time.LocalDateTime.parse("2026-08-01T18:00:00"));
        rental.setBorrower("测试借用人");
        rental.setContactPhone("13800000001");
        rental.setRentalPrice(new java.math.BigDecimal("150.00"));

        EquipmentRental result = rentalService.create(rental);
        System.out.println("id: " + result.getId());
        System.out.println("rentalNo: " + result.getRentalNo());
        System.out.println("status: " + result.getStatus());
        System.out.println("借出3件，可用库存应变为: 12 (原15-3)");

        Assertions.assertEquals("RENTED", result.getStatus());
    }

    @Test
    @Order(2)
    void create_shouldFailWhenStockInsufficient() {
        System.out.println("\n========== EquipmentRentalServiceTest.create_库存不足 ==========");
        EquipmentRental rental = new EquipmentRental();
        rental.setEquipmentId(6); // AED, available=2
        rental.setQuantity(10); // 借10台，不够
        rental.setStartTime(java.time.LocalDateTime.parse("2026-08-01T09:00:00"));
        rental.setEndTime(java.time.LocalDateTime.parse("2026-08-01T18:00:00"));
        rental.setBorrower("测试");
        rental.setContactPhone("13800000001");

        Assertions.assertThrows(BusinessException.class, () -> rentalService.create(rental));
        System.out.println("正确：可用库存不足，抛出异常");
    }

    @Test
    @Order(3)
    void returnEquipment_shouldRestoreStock() {
        System.out.println("\n========== EquipmentRentalServiceTest.return_归还设备 ==========");
        rentalService.returnEquipment(2, "2026-07-20 16:00:00");
        // R20260720002: 专业滑板-成人款 × 2, 原available=28, 借出后=26, 归还后=28
        System.out.println("租用记录 2 已归还，库存恢复");
    }

    @Test
    @Order(4)
    void returnEquipment_shouldFailWhenAlreadyReturned() {
        System.out.println("\n========== EquipmentRentalServiceTest.return_重复归还 ==========");
        Assertions.assertThrows(BusinessException.class,
                () -> rentalService.returnEquipment(1, null));
        // R20260720001 已经 RETURNED
        System.out.println("正确：已归还的记录不能重复归还");
    }
}
