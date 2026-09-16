package org.limitless.backend.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.limitless.backend.common.BusinessException;
import org.limitless.backend.common.PageResult;
import org.limitless.backend.entity.Equipment;
import org.limitless.backend.entity.EquipmentPurchase;
import org.limitless.backend.mapper.EquipmentMapper;
import org.limitless.backend.mapper.EquipmentPurchaseMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class EquipmentPurchaseService {

    private final EquipmentPurchaseMapper purchaseMapper;
    private final EquipmentMapper equipmentMapper;

    public EquipmentPurchaseService(EquipmentPurchaseMapper purchaseMapper, EquipmentMapper equipmentMapper) {
        this.purchaseMapper = purchaseMapper;
        this.equipmentMapper = equipmentMapper;
    }

    public PageResult<EquipmentPurchase> selectPage(String equipmentName, String purchaseDateStart,
                                                     String purchaseDateEnd, int pageNumber, int pageSize) {
        PageHelper.startPage(pageNumber, pageSize);
        List<EquipmentPurchase> list = purchaseMapper.selectPage(equipmentName, purchaseDateStart, purchaseDateEnd);
        PageInfo<EquipmentPurchase> pageInfo = new PageInfo<>(list);
        return new PageResult<>(pageInfo.getList(), pageNumber, pageSize, pageInfo.getTotal());
    }

    @Transactional
    public Integer create(EquipmentPurchase purchase) {
        if (purchase.getEquipmentId() == null) {
            throw new BusinessException("请选择关联设备");
        }
        if (purchase.getQuantity() == null || purchase.getQuantity() <= 0) {
            throw new BusinessException("购买数量必须大于0");
        }
        Equipment equipment = equipmentMapper.selectById(purchase.getEquipmentId());
        if (equipment == null) {
            throw new BusinessException("关联设备不存在");
        }
        purchase.setEquipmentName(equipment.getName());
        purchase.setBrand(equipment.getBrand());
        purchase.setModel(equipment.getModel());
        purchase.setSerialNumber(equipment.getSerialNumber());
        purchase.setPurchaseNo(generatePurchaseNo());
        purchaseMapper.insert(purchase);
        equipmentMapper.increaseStock(equipment.getId(), purchase.getQuantity());
        return purchase.getId();
    }

    private String generatePurchaseNo() {
        return "P" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
               + String.format("%03d", (int)(Math.random() * 1000));
    }
}
