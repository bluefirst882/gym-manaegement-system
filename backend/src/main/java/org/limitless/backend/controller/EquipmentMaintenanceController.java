package org.limitless.backend.controller;

import org.limitless.backend.common.PageResult;
import org.limitless.backend.common.Result;
import org.limitless.backend.entity.EquipmentMaintenance;
import org.limitless.backend.service.EquipmentMaintenanceService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class EquipmentMaintenanceController {

    private final EquipmentMaintenanceService maintenanceService;

    public EquipmentMaintenanceController(EquipmentMaintenanceService maintenanceService) {
        this.maintenanceService = maintenanceService;
    }

    @PostMapping("/equipment-maintenances")
    public Result<PageResult<EquipmentMaintenance>> listMaintenances(@RequestBody Map<String, Object> params) {
        int pageNumber = params.get("pageNumber") != null ? (int) params.get("pageNumber") : 1;
        int pageSize = params.get("pageSize") != null ? (int) params.get("pageSize") : 10;
        Integer equipmentId = (Integer) params.get("equipmentId");
        String maintenanceTimeStart = (String) params.get("maintenanceTimeStart");
        String maintenanceTimeEnd = (String) params.get("maintenanceTimeEnd");
        String equipmentCondition = (String) params.get("equipmentCondition");
        PageResult<EquipmentMaintenance> result = maintenanceService.selectPage(
                equipmentId, maintenanceTimeStart, maintenanceTimeEnd, equipmentCondition, pageNumber, pageSize);
        return Result.success("查询成功", result);
    }

    @PostMapping("/equipment-maintenance")
    public Result<Integer> createMaintenance(@RequestBody EquipmentMaintenance maintenance) {
        Integer id = maintenanceService.create(maintenance);
        return Result.success("新增成功", id);
    }

    @PutMapping("/equipment-maintenance/{id}")
    public Result<Void> updateMaintenance(@PathVariable Integer id, @RequestBody EquipmentMaintenance maintenance) {
        maintenanceService.update(id, maintenance);
        return Result.success("修改成功");
    }

    @DeleteMapping("/equipment-maintenance/{id}")
    public Result<Void> deleteMaintenance(@PathVariable Integer id) {
        maintenanceService.delete(id);
        return Result.success("删除成功");
    }
}
