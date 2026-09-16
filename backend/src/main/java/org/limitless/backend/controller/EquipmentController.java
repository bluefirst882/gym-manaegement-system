package org.limitless.backend.controller;

import org.limitless.backend.common.PageResult;
import org.limitless.backend.common.Result;
import org.limitless.backend.entity.Equipment;
import org.limitless.backend.service.EquipmentService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class EquipmentController {

    private final EquipmentService equipmentService;

    public EquipmentController(EquipmentService equipmentService) {
        this.equipmentService = equipmentService;
    }

    @PostMapping("/equipments")
    public Result<PageResult<Equipment>> listEquipments(@RequestBody Map<String, Object> params) {
        int pageNumber = params.get("pageNumber") != null ? (int) params.get("pageNumber") : 1;
        int pageSize = params.get("pageSize") != null ? (int) params.get("pageSize") : 10;
        String name = (String) params.get("name");
        Integer categoryId = (Integer) params.get("categoryId");
        String brand = (String) params.get("brand");
        String status = (String) params.get("status");
        PageResult<Equipment> result = equipmentService.selectPage(name, categoryId, brand, status, pageNumber, pageSize);
        return Result.success("查询成功", result);
    }

    @PostMapping("/equipment")
    public Result<Integer> createEquipment(@RequestBody Equipment equipment) {
        Integer id = equipmentService.create(equipment);
        return Result.success("新增成功", id);
    }

    @PutMapping("/equipment/{id}")
    public Result<Void> updateEquipment(@PathVariable Integer id, @RequestBody Equipment equipment) {
        equipmentService.update(id, equipment);
        return Result.success("修改成功");
    }

    @DeleteMapping("/equipment/{id}")
    public Result<Void> deleteEquipment(@PathVariable Integer id) {
        equipmentService.delete(id);
        return Result.success("删除成功");
    }
}
