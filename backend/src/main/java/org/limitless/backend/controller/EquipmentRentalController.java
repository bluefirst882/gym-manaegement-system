package org.limitless.backend.controller;

import org.limitless.backend.common.PageResult;
import org.limitless.backend.common.Result;
import org.limitless.backend.entity.EquipmentRental;
import org.limitless.backend.service.EquipmentRentalService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import jakarta.servlet.http.HttpServletRequest;
import org.limitless.backend.common.BusinessException;

@RestController
@RequestMapping("/api")
public class EquipmentRentalController {

    private final EquipmentRentalService rentalService;
    private final org.limitless.backend.service.RedisRequestGuard requestGuard;

    public EquipmentRentalController(EquipmentRentalService rentalService) {
        this.rentalService = rentalService;
        this.requestGuard = new org.limitless.backend.service.RedisRequestGuard();
    }

    @Autowired
    public EquipmentRentalController(EquipmentRentalService rentalService,
                                     org.limitless.backend.service.RedisRequestGuard requestGuard) {
        this.rentalService = rentalService;
        this.requestGuard = requestGuard;
    }

    @PostMapping("/equipment-rentals")
    public Result<PageResult<EquipmentRental>> listRentals(@RequestBody Map<String, Object> params, HttpServletRequest request) {
        requireAdmin(request);
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
    public Result<Object> createRental(@RequestBody EquipmentRental rental, HttpServletRequest request) {
        requireAdmin(request);
        int userId = currentUserId(request);
        requestGuard.checkRate("rental-create", userId);
        String requestId = request.getHeader("Idempotency-Key");
        var decision = requestGuard.begin("rental-create", userId, requestId);
        EquipmentRental result;
        if (!decision.acquired()) {
            result = rentalService.selectById(Integer.valueOf(decision.resultId()));
        } else {
            try {
                result = rentalService.create(rental);
                requestGuard.complete("rental-create", userId, requestId, String.valueOf(result.getId()));
            } catch (RuntimeException ex) {
                requestGuard.release("rental-create", userId, requestId);
                throw ex;
            }
        }
        return Result.success("租用成功", new java.util.HashMap<String, Object>() {{
            put("id", result.getId());
            put("rentalNo", result.getRentalNo());
            put("status", result.getStatus());
        }});
    }

    @PutMapping("/equipment-rental/{id}/return")
    public Result<Void> returnEquipment(@PathVariable Integer id, @RequestBody(required = false) Map<String, String> body,
                                        HttpServletRequest request) {
        requireAdmin(request);
        String actualReturnTime = body != null ? body.get("actualReturnTime") : null;
        rentalService.returnEquipment(id, actualReturnTime);
        return Result.success("归还成功");
    }

    private void requireAdmin(HttpServletRequest request) {
        if (request.getAttribute("currentUserId") == null) throw new BusinessException(401, "未登录");
        if (!"ADMIN".equals(request.getAttribute("currentRoleCode"))
                && !Integer.valueOf(1).equals(request.getAttribute("currentRoleId"))) {
            throw new BusinessException(403, "仅管理员可以执行此操作");
        }
    }

    private int currentUserId(HttpServletRequest request) {
        Integer userId = (Integer) request.getAttribute("currentUserId");
        if (userId == null) throw new BusinessException(401, "未登录");
        return userId;
    }
}
