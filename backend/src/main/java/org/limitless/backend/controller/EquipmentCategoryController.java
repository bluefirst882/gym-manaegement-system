package org.limitless.backend.controller;

import org.limitless.backend.common.PageRequest;
import org.limitless.backend.common.PageResult;
import org.limitless.backend.common.Result;
import org.limitless.backend.entity.EquipmentCategory;
import org.limitless.backend.service.EquipmentCategoryService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class EquipmentCategoryController {

    private final EquipmentCategoryService categoryService;

    public EquipmentCategoryController(EquipmentCategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/equipment-categories")
    public Result<PageResult<EquipmentCategory>> listCategories(@RequestBody PageRequest request) {
        PageResult<EquipmentCategory> result = categoryService.selectPage(request);
        return Result.success("查询成功", result);
    }
}
