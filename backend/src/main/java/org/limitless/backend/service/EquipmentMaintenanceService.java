package org.limitless.backend.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.limitless.backend.common.BusinessException;
import org.limitless.backend.common.PageResult;
import org.limitless.backend.entity.Equipment;
import org.limitless.backend.entity.EquipmentMaintenance;
import org.limitless.backend.mapper.EquipmentMapper;
import org.limitless.backend.mapper.EquipmentMaintenanceMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class EquipmentMaintenanceService {

    private final EquipmentMaintenanceMapper maintenanceMapper;
    private final EquipmentMapper equipmentMapper;

    public EquipmentMaintenanceService(EquipmentMaintenanceMapper maintenanceMapper,
                                       EquipmentMapper equipmentMapper) {
        this.maintenanceMapper = maintenanceMapper;
        this.equipmentMapper = equipmentMapper;
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

    @Transactional
    public Integer create(EquipmentMaintenance maintenance) {
        if (maintenance.getTitle() == null || maintenance.getTitle().trim().isEmpty()) {
            throw new BusinessException("维护标题不能为空");
        }
        if (maintenance.getEquipmentId() == null) {
            throw new BusinessException("请选择维护设备");
        }
        if (maintenance.getMaintenanceTime() == null) {
            throw new BusinessException("维护时间不能为空");
        }
        if (maintenance.getContent() == null || maintenance.getContent().trim().isEmpty()) {
            throw new BusinessException("维护内容不能为空");
        }
        if (maintenance.getPersonnel() == null || maintenance.getPersonnel().trim().isEmpty()) {
            throw new BusinessException("维护人员不能为空");
        }
        Equipment equipment = equipmentMapper.selectById(maintenance.getEquipmentId());
        if (equipment == null) {
            throw new BusinessException("维护设备不存在");
        }
        if (maintenance.getEquipmentCondition() == null || maintenance.getEquipmentCondition().isBlank()) {
            maintenance.setEquipmentCondition("NORMAL");
        }
        maintenance.setMaintenanceNo(generateMaintenanceNo());
        maintenanceMapper.insert(maintenance);
        Equipment equipmentUpdate = new Equipment();
        equipmentUpdate.setId(equipment.getId());
        equipmentUpdate.setStatus(switch (maintenance.getEquipmentCondition()) {
            case "REPAIRING" -> "FAULT";
            case "SCRAPPED" -> "SCRAPPED";
            default -> "NORMAL";
        });
        equipmentMapper.updateById(equipmentUpdate);
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
