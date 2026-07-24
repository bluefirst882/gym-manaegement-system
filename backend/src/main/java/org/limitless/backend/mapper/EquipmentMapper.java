package org.limitless.backend.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.limitless.backend.entity.Equipment;
import java.util.List;

@Mapper
public interface EquipmentMapper {
    Equipment selectById(Integer id);
    List<Equipment> selectPage(@Param("name") String name,
                               @Param("categoryId") Integer categoryId,
                               @Param("brand") String brand,
                               @Param("status") String status);
    long countPage(@Param("name") String name,
                   @Param("categoryId") Integer categoryId,
                   @Param("brand") String brand,
                   @Param("status") String status);
    int insert(Equipment equipment);
    int updateById(Equipment equipment);
    int increaseStock(@Param("id") Integer id, @Param("quantity") Integer quantity);
    int decreaseAvailableStock(@Param("id") Integer id, @Param("quantity") Integer quantity);
    int increaseAvailableStock(@Param("id") Integer id, @Param("quantity") Integer quantity);
    int deleteById(Integer id);
}
