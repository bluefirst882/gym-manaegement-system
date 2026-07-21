package org.limitless.backend.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.limitless.backend.entity.ActivityCategory;
import org.limitless.backend.mapper.ActivityCategoryMapper;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ActivityCategoryServiceTest {

    @Mock
    private ActivityCategoryMapper categoryMapper;

    private ActivityCategoryService categoryService;

    @BeforeEach
    void setUp() {
        categoryService = new ActivityCategoryService(categoryMapper);
    }
}
