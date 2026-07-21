package org.limitless.backend.controller;

import org.limitless.backend.common.PageResult;
import org.limitless.backend.common.Result;
import org.limitless.backend.dto.AuditRequest;
import org.limitless.backend.dto.RegistrationCreateRequest;
import org.limitless.backend.dto.RegistrationPageRequest;
import org.limitless.backend.entity.ActivityRegistration;
import org.limitless.backend.service.ActivityRegistrationService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ActivityRegistrationController {

    private final ActivityRegistrationService registrationService;

    public ActivityRegistrationController(ActivityRegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    /**
     * 分页查询报名记录
     * POST /api/activity-registrations
     */
    @PostMapping("/activity-registrations")
    public Result<PageResult<ActivityRegistration>> listRegistrations(@RequestBody RegistrationPageRequest request) {
        PageResult<ActivityRegistration> result = registrationService.selectPage(request);
        return Result.success("查询成功", result);
    }

    /**
     * 新增报名
     * POST /api/activity-registration
     */
    @PostMapping("/activity-registration")
    public Result<Object> createRegistration(@RequestBody RegistrationCreateRequest request) {
        ActivityRegistration registration = registrationService.create(request);
        return Result.success("报名成功", new java.util.HashMap<String, Object>() {{
            put("id", registration.getId());
            put("registrationNo", registration.getRegistrationNo());
            put("auditStatus", registration.getAuditStatus());
        }});
    }

    /**
     * 审核报名
     * PUT /api/activity-registration/{id}/audit
     */
    @PutMapping("/activity-registration/{id}/audit")
    public Result<Void> auditRegistration(@PathVariable Long id, @RequestBody AuditRequest request) {
        registrationService.audit(id, request);
        return Result.success("审核成功");
    }

    /**
     * 客户取消待审核报名
     * PUT /api/activity-registration/{id}/cancel
     */
    @PutMapping("/activity-registration/{id}/cancel")
    public Result<Void> cancelRegistration(@PathVariable Long id) {
        registrationService.cancel(id);
        return Result.success("取消成功");
    }

    /**
     * 删除报名
     * DELETE /api/activity-registration/{id}
     */
    @DeleteMapping("/activity-registration/{id}")
    public Result<Void> deleteRegistration(@PathVariable Long id) {
        registrationService.delete(id);
        return Result.success("删除成功");
    }
}
