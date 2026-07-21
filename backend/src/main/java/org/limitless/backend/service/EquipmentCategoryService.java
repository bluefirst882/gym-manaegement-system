package org.limitless.backend.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.limitless.backend.common.PageRequest;
import org.limitless.backend.common.PageResult;
import org.limitless.backend.entity.EquipmentCategory;
import org.limitless.backend.mapper.EquipmentCategoryMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EquipmentCategoryService {

    private final EquipmentCategoryMapper categoryMapper;

    public EquipmentCategoryService(EquipmentCategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    public PageResult<EquipmentCategory> selectPage(PageRequest request) {
        PageHelper.startPage(request.getPageNumber(), request.getPageSize());
        List<EquipmentCategory> list = categoryMapper.selectPage(null);
        PageInfo<EquipmentCategory> pageInfo = new PageInfo<>(list);
        return new PageResult<>(pageInfo.getList(), request.getPageNumber(),
                request.getPageSize(), pageInfo.getTotal());
    }
}
