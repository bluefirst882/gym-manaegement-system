package org.limitless.backend.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.limitless.backend.entity.Activity;
import java.util.List;

@Mapper
public interface ActivityMapper {
    Activity selectById(Integer id);
    List<Activity> selectPage(@Param("title") String title,
                              @Param("categoryId") Integer categoryId,
                              @Param("status") String status);
    int insert(Activity activity);
    int updateById(Activity activity);
    int deleteById(Integer id);
}
