package org.limitless.backend.service;

import org.junit.jupiter.api.*;
import org.limitless.backend.entity.EquipmentPurchase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class EquipmentPurchaseServiceTest {

    @Autowired
    private EquipmentPurchaseService purchaseService;

    @Test
    @Order(1)
    void create_shouldInsertPurchase() {
        System.out.println("\n========== EquipmentPurchaseServiceTest.create ==========");
        EquipmentPurchase purchase = new EquipmentPurchase();
        purchase.setEquipmentId(3);
        purchase.setEquipmentName("攀岩头盔");
        purchase.setBrand("Petzl");
        purchase.setModel("ELIA New");
        purchase.setSerialNumber("PT20260010");
        purchase.setQuantity(10);
        purchase.setPurchasePrice(new java.math.BigDecimal("3000.00"));
        purchase.setPurchaseDate(java.time.LocalDate.parse("2026-07-01"));
        purchase.setSupplier("北京户外装备有限公司");

        Integer id = purchaseService.create(purchase);
        System.out.println("created purchaseId: " + id);
        System.out.println("purchaseNo: " + purchase.getPurchaseNo());

        Assertions.assertNotNull(id);
    }
}
