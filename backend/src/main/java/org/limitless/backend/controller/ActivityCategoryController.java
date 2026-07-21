package org.limitless.backend.controller;

import org.limitless.backend.common.PageRequest;
import org.limitless.backend.common.PageResult;
import org.limitless.backend.common.Result;
import org.limitless.backend.entity.ActivityCategory;
import org.limitless.backend.service.ActivityCategoryService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ActivityCategoryController {

    private final ActivityCategoryService categoryService;

    public ActivityCategoryController(ActivityCategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * 分页查询活动分类
     * POST /api/activity-categories
     */
    @PostMapping("/activity-categories")
    public Result<PageResult<ActivityCategory>> listCategories(@RequestBody PageRequest request) {
        PageResult<ActivityCategory> result = categoryService.selectPage(request);
        return Result.success("查询成功", result);
    }
}
