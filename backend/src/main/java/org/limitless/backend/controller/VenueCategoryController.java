package org.limitless.backend.controller;

import org.limitless.backend.common.PageResult;
import org.limitless.backend.common.Result;
import org.limitless.backend.dto.VenueCategoryPageRequest;
import org.limitless.backend.entity.VenueCategory;
import org.limitless.backend.service.VenueCategoryService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class VenueCategoryController {

    private final VenueCategoryService categoryService;

    public VenueCategoryController(VenueCategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * 分页查询场地分类
     * POST /api/venue-categories
     */
    @PostMapping("/venue-categories")
    public Result<PageResult<VenueCategory>> listCategories(@RequestBody VenueCategoryPageRequest request) {
        PageResult<VenueCategory> result = categoryService.selectPage(request);
        return Result.success("查询成功", result);
    }

    /**
     * 新增场地分类
     * POST /api/venue-category
     */
    @PostMapping("/venue-category")
    public Result<Integer> createCategory(@RequestBody VenueCategory category) {
        Integer id = categoryService.create(category);
        return Result.success("新增成功", id);
    }

    /**
     * 修改场地分类
     * PUT /api/venue-category/{id}
     */
    @PutMapping("/venue-category/{id}")
    public Result<Void> updateCategory(@PathVariable Integer id, @RequestBody VenueCategory category) {
        categoryService.update(id, category);
        return Result.success("修改成功");
    }

    /**
     * 删除场地分类
     * DELETE /api/venue-category/{id}
     */
    @DeleteMapping("/venue-category/{id}")
    public Result<Void> deleteCategory(@PathVariable Integer id) {
        categoryService.delete(id);
        return Result.success("删除成功");
    }
}
