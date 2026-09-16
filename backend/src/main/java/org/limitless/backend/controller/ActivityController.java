package org.limitless.backend.controller;

import org.limitless.backend.common.PageResult;
import org.limitless.backend.common.Result;
import org.limitless.backend.dto.ActivityCreateRequest;
import org.limitless.backend.dto.ActivityPageRequest;
import org.limitless.backend.dto.ActivityUpdateRequest;
import org.limitless.backend.entity.Activity;
import org.limitless.backend.service.ActivityService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ActivityController {

    private final ActivityService activityService;

    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    /**
     * 分页查询活动
     * POST /api/activities
     */
    @PostMapping("/activities")
    public Result<PageResult<Activity>> listActivities(@RequestBody ActivityPageRequest request) {
        PageResult<Activity> result = activityService.selectPage(request);
        return Result.success("查询成功", result);
    }

    /**
     * 查询活动详情（微信小程序使用）
     * GET /api/activity/{id}
     */
    @GetMapping("/activity/{id}")
    public Result<Activity> getActivity(@PathVariable Integer id) {
        return Result.success("查询成功", activityService.selectById(id));
    }

    /**
     * 新增活动
     * POST /api/activity
     */
    @PostMapping("/activity")
    public Result<Integer> createActivity(@RequestBody ActivityCreateRequest request) {
        Integer id = activityService.create(request);
        return Result.success("新增成功", id);
    }

    /**
     * 修改活动
     * PUT /api/activity/{id}
     */
    @PutMapping("/activity/{id}")
    public Result<Void> updateActivity(@PathVariable Integer id, @RequestBody ActivityUpdateRequest request) {
        activityService.update(id, request);
        return Result.success("修改成功");
    }

    /**
     * 删除活动
     * DELETE /api/activity/{id}
     */
    @DeleteMapping("/activity/{id}")
    public Result<Void> deleteActivity(@PathVariable Integer id) {
        activityService.delete(id);
        return Result.success("删除成功");
    }
}
