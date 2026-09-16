package org.limitless.backend.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.limitless.backend.common.BusinessException;
import org.limitless.backend.common.PageResult;
import org.limitless.backend.entity.Equipment;
import org.limitless.backend.mapper.EquipmentMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class EquipmentService {

    private final EquipmentMapper equipmentMapper;

    public EquipmentService(EquipmentMapper equipmentMapper) {
        this.equipmentMapper = equipmentMapper;
    }

    public PageResult<Equipment> selectPage(String name, Integer categoryId, String brand, String status,
                                            int pageNumber, int pageSize) {
        PageHelper.startPage(pageNumber, pageSize);
        List<Equipment> list = equipmentMapper.selectPage(name, categoryId, brand, status);
        PageInfo<Equipment> pageInfo = new PageInfo<>(list);
        return new PageResult<>(pageInfo.getList(), pageNumber, pageSize, pageInfo.getTotal());
    }

    public Integer create(Equipment equipment) {
        if (equipment.getName() == null || equipment.getName().trim().isEmpty()) {
            throw new BusinessException("设备名称不能为空");
        }
        equipment.setEquipmentNo(generateEquipmentNo());
        if (equipment.getAvailableQuantity() == null) {
            equipment.setAvailableQuantity(equipment.getQuantity());
        }
        if (equipment.getStatus() == null) {
            equipment.setStatus("NORMAL");
        }
        equipmentMapper.insert(equipment);
        return equipment.getId();
    }

    public void update(Integer id, Equipment equipment) {
        Equipment existing = equipmentMapper.selectById(id);
        if (existing == null) {
            throw new BusinessException("设备不存在");
        }
        equipment.setId(id);
        equipmentMapper.updateById(equipment);
    }

    public void delete(Integer id) {
        Equipment existing = equipmentMapper.selectById(id);
        if (existing == null) {
            throw new BusinessException("设备不存在");
        }
        equipmentMapper.deleteById(id);
    }

    private String generateEquipmentNo() {
        return "E" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
               + String.format("%03d", (int)(Math.random() * 1000));
    }
}
