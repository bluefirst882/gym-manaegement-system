package org.limitless.backend.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.limitless.backend.common.BusinessException;
import org.limitless.backend.common.PageResult;
import org.limitless.backend.entity.Equipment;
import org.limitless.backend.entity.EquipmentRental;
import org.limitless.backend.mapper.EquipmentMapper;
import org.limitless.backend.mapper.EquipmentRentalMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class EquipmentRentalService {

    private final EquipmentRentalMapper rentalMapper;
    private final EquipmentMapper equipmentMapper;

    public EquipmentRentalService(EquipmentRentalMapper rentalMapper, EquipmentMapper equipmentMapper) {
        this.rentalMapper = rentalMapper;
        this.equipmentMapper = equipmentMapper;
    }

    public PageResult<EquipmentRental> selectPage(String borrower, Integer equipmentId, String status,
                                                   String startTimeStart, String startTimeEnd,
                                                   int pageNumber, int pageSize) {
        PageHelper.startPage(pageNumber, pageSize);
        List<EquipmentRental> list = rentalMapper.selectPage(borrower, equipmentId, status,
                startTimeStart, startTimeEnd);
        PageInfo<EquipmentRental> pageInfo = new PageInfo<>(list);
        return new PageResult<>(pageInfo.getList(), pageNumber, pageSize, pageInfo.getTotal());
    }

    public EquipmentRental selectById(Integer id) {
        return rentalMapper.selectById(id);
    }

    @Transactional
    public EquipmentRental create(EquipmentRental rental) {
        if (rental.getEquipmentId() == null) {
            throw new BusinessException("设备ID不能为空");
        }

        Equipment equipment = equipmentMapper.selectById(rental.getEquipmentId());
        if (equipment == null) {
            throw new BusinessException("设备不存在");
        }

        int requestedQty = rental.getQuantity() != null ? rental.getQuantity() : 1;
        if (requestedQty <= 0) {
            throw new BusinessException("租用数量必须大于0");
        }
        if (equipmentMapper.decreaseAvailableStock(equipment.getId(), requestedQty) == 0) {
            Equipment latest = equipmentMapper.selectById(rental.getEquipmentId());
            int available = latest == null ? 0 : latest.getAvailableQuantity();
            throw new BusinessException("设备可用库存不足，当前可用: " + available);
        }

        rental.setQuantity(requestedQty);
        rental.setRentalNo(generateRentalNo());
        rental.setStatus("RENTED");
        rentalMapper.insert(rental);

        return rental;
    }

    @Transactional
    public void returnEquipment(Integer id, String actualReturnTime) {
        EquipmentRental rental = rentalMapper.selectById(id);
        if (rental == null) {
            throw new BusinessException("租用记录不存在");
        }
        if (!"RENTED".equals(rental.getStatus())) {
            throw new BusinessException("该设备已归还");
        }

        LocalDateTime returnTime = actualReturnTime != null
                ? LocalDateTime.parse(actualReturnTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                : LocalDateTime.now();
        if (rentalMapper.returnIfRented(id, returnTime) != 1) {
            throw new BusinessException("该设备已归还或状态已发生变化");
        }

        // 恢复库存
        Equipment equipment = equipmentMapper.selectById(rental.getEquipmentId());
        if (equipment != null) {
            equipmentMapper.increaseAvailableStock(equipment.getId(), rental.getQuantity());
        }
    }

    private String generateRentalNo() {
        return "R" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
               + String.format("%03d", (int)(Math.random() * 1000));
    }
}
