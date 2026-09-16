package org.limitless.backend.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.limitless.backend.entity.EquipmentPurchase;
import java.util.List;

@Mapper
public interface EquipmentPurchaseMapper {
    EquipmentPurchase selectById(Integer id);
    List<EquipmentPurchase> selectPage(@Param("equipmentName") String equipmentName,
                                       @Param("purchaseDateStart") String purchaseDateStart,
                                       @Param("purchaseDateEnd") String purchaseDateEnd);
    long countPage(@Param("equipmentName") String equipmentName,
                   @Param("purchaseDateStart") String purchaseDateStart,
                   @Param("purchaseDateEnd") String purchaseDateEnd);
    int insert(EquipmentPurchase purchase);
}
