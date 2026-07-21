package org.limitless.backend.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.limitless.backend.entity.EquipmentCategory;
import java.util.List;

@Mapper
public interface EquipmentCategoryMapper {
    EquipmentCategory selectById(Integer id);
    List<EquipmentCategory> selectPage(@Param("categoryName") String categoryName);
    long countPage(@Param("categoryName") String categoryName);
    int insert(EquipmentCategory category);
    int updateById(EquipmentCategory category);
    int deleteById(Integer id);
}
