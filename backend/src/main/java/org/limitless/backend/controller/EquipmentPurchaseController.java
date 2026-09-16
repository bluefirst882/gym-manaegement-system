package org.limitless.backend.controller;

import org.limitless.backend.common.PageResult;
import org.limitless.backend.common.Result;
import org.limitless.backend.entity.EquipmentPurchase;
import org.limitless.backend.service.EquipmentPurchaseService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class EquipmentPurchaseController {

    private final EquipmentPurchaseService purchaseService;

    public EquipmentPurchaseController(EquipmentPurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @PostMapping("/equipment-purchases")
    public Result<PageResult<EquipmentPurchase>> listPurchases(@RequestBody Map<String, Object> params) {
        int pageNumber = params.get("pageNumber") != null ? (int) params.get("pageNumber") : 1;
        int pageSize = params.get("pageSize") != null ? (int) params.get("pageSize") : 10;
        String equipmentName = (String) params.get("equipmentName");
        String purchaseDateStart = (String) params.get("purchaseDateStart");
        String purchaseDateEnd = (String) params.get("purchaseDateEnd");
        PageResult<EquipmentPurchase> result = purchaseService.selectPage(
                equipmentName, purchaseDateStart, purchaseDateEnd, pageNumber, pageSize);
        return Result.success("查询成功", result);
    }

    @PostMapping("/equipment-purchase")
    public Result<Integer> createPurchase(@RequestBody EquipmentPurchase purchase) {
        Integer id = purchaseService.create(purchase);
        return Result.success("新增成功", id);
    }
}
