package org.limitless.backend.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.limitless.backend.common.BusinessException;
import org.limitless.backend.common.PageResult;
import org.limitless.backend.dto.VenueCategoryPageRequest;
import org.limitless.backend.entity.VenueCategory;
import org.limitless.backend.mapper.VenueCategoryMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.time.LocalDateTime;

@Service
public class VenueCategoryService {

    private final VenueCategoryMapper categoryMapper;

    public VenueCategoryService(VenueCategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    public PageResult<VenueCategory> selectPage(VenueCategoryPageRequest request) {
        PageHelper.startPage(request.getPageNumber(), request.getPageSize());
        List<VenueCategory> list = categoryMapper.selectPage(request.getCategoryName());
        PageInfo<VenueCategory> pageInfo = new PageInfo<>(list);
        return new PageResult<>(pageInfo.getList(), request.getPageNumber(),
                request.getPageSize(), pageInfo.getTotal());
    }

    public Integer create(VenueCategory category) {
        if (category.getCategoryName() == null || category.getCategoryName().trim().isEmpty()) {
            throw new BusinessException("分类名称不能为空");
        }
        categoryMapper.insert(category);
        return category.getId();
    }

    public void update(Integer id, VenueCategory category) {
        VenueCategory existing = categoryMapper.selectById(id);
        if (existing == null) {
            throw new BusinessException("分类不存在");
        }
        category.setId(id);
        categoryMapper.updateById(category);
    }

    public void delete(Integer id) {
        VenueCategory existing = categoryMapper.selectById(id);
        if (existing == null) {
            throw new BusinessException("分类不存在");
        }
        int count = categoryMapper.countVenueByCategoryId(id);
        if (count > 0) {
            throw new BusinessException("该分类下有场地，无法删除");
        }
        categoryMapper.deleteById(id);
    }
}
