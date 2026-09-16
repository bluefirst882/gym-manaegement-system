package org.limitless.backend.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.limitless.backend.entity.EquipmentMaintenance;
import java.util.List;

@Mapper
public interface EquipmentMaintenanceMapper {
    EquipmentMaintenance selectById(Integer id);
    List<EquipmentMaintenance> selectPage(@Param("equipmentId") Integer equipmentId,
                                          @Param("maintenanceTimeStart") String maintenanceTimeStart,
                                          @Param("maintenanceTimeEnd") String maintenanceTimeEnd,
                                          @Param("equipmentCondition") String equipmentCondition);
    long countPage(@Param("equipmentId") Integer equipmentId,
                   @Param("maintenanceTimeStart") String maintenanceTimeStart,
                   @Param("maintenanceTimeEnd") String maintenanceTimeEnd,
                   @Param("equipmentCondition") String equipmentCondition);
    int insert(EquipmentMaintenance maintenance);
    int updateById(EquipmentMaintenance maintenance);
    int deleteById(Integer id);
}
