package org.limitless.backend.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.limitless.backend.common.BusinessException;
import org.limitless.backend.common.PageResult;
import org.limitless.backend.entity.EquipmentPurchase;
import org.limitless.backend.mapper.EquipmentPurchaseMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class EquipmentPurchaseService {

    private final EquipmentPurchaseMapper purchaseMapper;

    public EquipmentPurchaseService(EquipmentPurchaseMapper purchaseMapper) {
        this.purchaseMapper = purchaseMapper;
    }

    public PageResult<EquipmentPurchase> selectPage(String equipmentName, String purchaseDateStart,
                                                     String purchaseDateEnd, int pageNumber, int pageSize) {
        PageHelper.startPage(pageNumber, pageSize);
        List<EquipmentPurchase> list = purchaseMapper.selectPage(equipmentName, purchaseDateStart, purchaseDateEnd);
        PageInfo<EquipmentPurchase> pageInfo = new PageInfo<>(list);
        return new PageResult<>(pageInfo.getList(), pageNumber, pageSize, pageInfo.getTotal());
    }

    public Integer create(EquipmentPurchase purchase) {
        purchase.setPurchaseNo(generatePurchaseNo());
        purchaseMapper.insert(purchase);
        return purchase.getId();
    }

    private String generatePurchaseNo() {
        return "P" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
               + String.format("%03d", (int)(Math.random() * 1000));
    }
}
