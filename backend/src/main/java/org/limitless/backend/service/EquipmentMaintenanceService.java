package org.limitless.backend.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.limitless.backend.common.BusinessException;
import org.limitless.backend.common.PageResult;
import org.limitless.backend.entity.EquipmentMaintenance;
import org.limitless.backend.mapper.EquipmentMaintenanceMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class EquipmentMaintenanceService {

    private final EquipmentMaintenanceMapper maintenanceMapper;

    public EquipmentMaintenanceService(EquipmentMaintenanceMapper maintenanceMapper) {
        this.maintenanceMapper = maintenanceMapper;
    }

    public PageResult<EquipmentMaintenance> selectPage(Integer equipmentId, String maintenanceTimeStart,
                                                        String maintenanceTimeEnd, String equipmentCondition,
                                                        int pageNumber, int pageSize) {
        PageHelper.startPage(pageNumber, pageSize);
        List<EquipmentMaintenance> list = maintenanceMapper.selectPage(equipmentId, maintenanceTimeStart,
                maintenanceTimeEnd, equipmentCondition);
        PageInfo<EquipmentMaintenance> pageInfo = new PageInfo<>(list);
        return new PageResult<>(pageInfo.getList(), pageNumber, pageSize, pageInfo.getTotal());
    }

    public Integer create(EquipmentMaintenance maintenance) {
        maintenance.setMaintenanceNo(generateMaintenanceNo());
        maintenanceMapper.insert(maintenance);
        return maintenance.getId();
    }

    public void update(Integer id, EquipmentMaintenance maintenance) {
        EquipmentMaintenance existing = maintenanceMapper.selectById(id);
        if (existing == null) {
            throw new BusinessException("维护记录不存在");
        }
        maintenance.setId(id);
        maintenanceMapper.updateById(maintenance);
    }

    public void delete(Integer id) {
        EquipmentMaintenance existing = maintenanceMapper.selectById(id);
        if (existing == null) {
            throw new BusinessException("维护记录不存在");
        }
        maintenanceMapper.deleteById(id);
    }

    private String generateMaintenanceNo() {
        return "M" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
               + String.format("%03d", (int)(Math.random() * 1000));
    }
}
