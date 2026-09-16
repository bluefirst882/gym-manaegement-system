package org.limitless.backend.controller;

import org.limitless.backend.common.PageResult;
import org.limitless.backend.common.Result;
import org.limitless.backend.entity.EquipmentRental;
import org.limitless.backend.service.EquipmentRentalService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class EquipmentRentalController {

    private final EquipmentRentalService rentalService;

    public EquipmentRentalController(EquipmentRentalService rentalService) {
        this.rentalService = rentalService;
    }

    @PostMapping("/equipment-rentals")
    public Result<PageResult<EquipmentRental>> listRentals(@RequestBody Map<String, Object> params) {
        int pageNumber = params.get("pageNumber") != null ? (int) params.get("pageNumber") : 1;
        int pageSize = params.get("pageSize") != null ? (int) params.get("pageSize") : 10;
        String borrower = (String) params.get("borrower");
        Integer equipmentId = (Integer) params.get("equipmentId");
        String status = (String) params.get("status");
        String startTimeStart = (String) params.get("startTimeStart");
        String startTimeEnd = (String) params.get("startTimeEnd");
        PageResult<EquipmentRental> result = rentalService.selectPage(
                borrower, equipmentId, status, startTimeStart, startTimeEnd, pageNumber, pageSize);
        return Result.success("查询成功", result);
    }

    @PostMapping("/equipment-rental")
    public Result<Object> createRental(@RequestBody EquipmentRental rental) {
        EquipmentRental result = rentalService.create(rental);
        return Result.success("租用成功", new java.util.HashMap<String, Object>() {{
            put("id", result.getId());
            put("rentalNo", result.getRentalNo());
            put("status", result.getStatus());
        }});
    }

    @PutMapping("/equipment-rental/{id}/return")
    public Result<Void> returnEquipment(@PathVariable Integer id, @RequestBody(required = false) Map<String, String> body) {
        String actualReturnTime = body != null ? body.get("actualReturnTime") : null;
        rentalService.returnEquipment(id, actualReturnTime);
        return Result.success("归还成功");
    }
}
