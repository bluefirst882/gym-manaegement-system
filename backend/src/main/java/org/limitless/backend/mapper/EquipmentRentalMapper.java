package org.limitless.backend.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.limitless.backend.entity.EquipmentRental;
import java.util.List;

@Mapper
public interface EquipmentRentalMapper {
    EquipmentRental selectById(Integer id);
    List<EquipmentRental> selectPage(@Param("borrower") String borrower,
                                     @Param("equipmentId") Integer equipmentId,
                                     @Param("status") String status,
                                     @Param("startTimeStart") String startTimeStart,
                                     @Param("startTimeEnd") String startTimeEnd);
    long countPage(@Param("borrower") String borrower,
                   @Param("equipmentId") Integer equipmentId,
                   @Param("status") String status,
                   @Param("startTimeStart") String startTimeStart,
                   @Param("startTimeEnd") String startTimeEnd);
    int insert(EquipmentRental rental);
    int updateById(EquipmentRental rental);

    int returnIfRented(@Param("id") Integer id, @Param("actualReturnTime") java.time.LocalDateTime actualReturnTime);
}
