package org.limitless.backend.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.limitless.backend.entity.VenueCategory;

import java.util.List;

@Mapper
public interface VenueCategoryMapper {
    VenueCategory selectById(Integer id);

    List<VenueCategory> selectPage(@Param("categoryName") String categoryName);

    long countPage(@Param("categoryName") String categoryName);

    int insert(VenueCategory category);

    int updateById(VenueCategory category);

    int deleteById(Integer id);

    int countVenueByCategoryId(Integer categoryId);
}
