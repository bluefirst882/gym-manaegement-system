package org.limitless.backend.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.limitless.backend.entity.ActivityCategory;
import java.util.List;

@Mapper
public interface ActivityCategoryMapper {
    ActivityCategory selectById(Integer id);
    List<ActivityCategory> selectPage(@Param("categoryName") String categoryName);
    int insert(ActivityCategory category);
    int updateById(ActivityCategory category);
    int deleteById(Integer id);
}
