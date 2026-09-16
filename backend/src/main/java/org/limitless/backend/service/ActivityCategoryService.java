package org.limitless.backend.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.limitless.backend.common.BusinessException;
import org.limitless.backend.common.PageRequest;
import org.limitless.backend.common.PageResult;
import org.limitless.backend.entity.ActivityCategory;
import org.limitless.backend.mapper.ActivityCategoryMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityCategoryService {

    private final ActivityCategoryMapper categoryMapper;

    public ActivityCategoryService(ActivityCategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    public PageResult<ActivityCategory> selectPage(PageRequest request) {
        PageHelper.startPage(request.getPageNumber(), request.getPageSize());
        List<ActivityCategory> list = categoryMapper.selectPage(null);
        PageInfo<ActivityCategory> pageInfo = new PageInfo<>(list);
        return new PageResult<>(pageInfo.getList(), request.getPageNumber(),
                request.getPageSize(), pageInfo.getTotal());
    }
}
