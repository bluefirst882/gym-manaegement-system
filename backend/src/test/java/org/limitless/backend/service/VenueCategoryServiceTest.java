package org.limitless.backend.service;

import org.junit.jupiter.api.*;
import org.limitless.backend.common.BusinessException;
import org.limitless.backend.entity.VenueCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class VenueCategoryServiceTest {

    @Autowired
    private VenueCategoryService categoryService;

    @Test
    @Order(1)
    void create_shouldInsertCategory() {
        System.out.println("\n========== VenueCategoryServiceTest.create ==========");
        VenueCategory category = new VenueCategory();
        category.setCategoryCode("VC_TEST");
        category.setCategoryName("测试分类");
        category.setCategoryDesc("这是一个测试分类");
        category.setSortOrder(99);

        Integer id = categoryService.create(category);
        System.out.println("created categoryId: " + id + ", name: " + category.getCategoryName());
        Assertions.assertNotNull(id);
    }

    @Test
    @Order(2)
    void update_shouldModifyCategory() {
        System.out.println("\n========== VenueCategoryServiceTest.update ==========");
        VenueCategory update = new VenueCategory();
        update.setCategoryName("攀岩与高空类");
        update.setCategoryDesc("包含攀岩和高空拓展场地");

        categoryService.update(1, update);
        System.out.println("分类 1 更新成功");
    }

    @Test
    @Order(3)
    void delete_shouldFailWhenVenueExists() {
        System.out.println("\n========== VenueCategoryServiceTest.delete_shouldFail ==========");
        Assertions.assertThrows(BusinessException.class, () -> categoryService.delete(1));
        System.out.println("正确：分类 1 下有场地，禁止删除");
    }
}
