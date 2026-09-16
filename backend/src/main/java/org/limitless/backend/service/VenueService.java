package org.limitless.backend.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.limitless.backend.common.BusinessException;
import org.limitless.backend.common.PageResult;
import org.limitless.backend.dto.VenueCreateRequest;
import org.limitless.backend.dto.VenuePageRequest;
import org.limitless.backend.dto.VenueUpdateRequest;
import org.limitless.backend.entity.Venue;
import org.limitless.backend.mapper.VenueMapper;
import org.limitless.backend.mapper.VenueCategoryMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class VenueService {

    private final VenueMapper venueMapper;
    private final VenueCategoryMapper categoryMapper;

    public VenueService(VenueMapper venueMapper, VenueCategoryMapper categoryMapper) {
        this.venueMapper = venueMapper;
        this.categoryMapper = categoryMapper;
    }

    public PageResult<Venue> selectPage(VenuePageRequest request) {
        PageHelper.startPage(request.getPageNumber(), request.getPageSize());
        List<Venue> list = venueMapper.selectPage(
                request.getVenueName(), request.getVenueAddress(),
                request.getCategoryId(), request.getStatus());
        PageInfo<Venue> pageInfo = new PageInfo<>(list);
        return new PageResult<>(pageInfo.getList(), request.getPageNumber(),
                request.getPageSize(), pageInfo.getTotal());
    }

    public Venue selectById(Integer id) {
        Venue venue = venueMapper.selectById(id);
        if (venue == null) {
            throw new BusinessException("场地不存在");
        }
        return venue;
    }

    public Integer create(VenueCreateRequest request) {
        if (request.getVenueName() == null || request.getVenueName().trim().isEmpty()) {
            throw new BusinessException("场地名称不能为空");
        }

        Venue venue = new Venue();
        venue.setVenueCode(generateVenueCode());
        venue.setVenueName(request.getVenueName());
        venue.setVenueAddress(request.getVenueAddress());
        venue.setContactPhone(request.getContactPhone());
        venue.setCategoryId(request.getCategoryId());
        venue.setDimensions(request.getDimensions());
        venue.setMaterial(request.getMaterial());
        venue.setCapacity(request.getCapacity());
        venue.setFacilities(request.getFacilities());
        venue.setFeeType(request.getFeeType() != null ? request.getFeeType() : "PER_HOUR");
        venue.setFeeAmount(request.getFeeAmount() != null ? request.getFeeAmount() : java.math.BigDecimal.ZERO);
        venue.setCoverImage(request.getCoverImage());
        venue.setStatus(request.getStatus() != null ? request.getStatus() : "AVAILABLE");
        venue.setRemark(request.getRemark());

        venueMapper.insert(venue);
        return venue.getId();
    }

    public void update(Integer id, VenueUpdateRequest request) {
        Venue existing = venueMapper.selectById(id);
        if (existing == null) {
            throw new BusinessException("场地不存在");
        }

        Venue venue = new Venue();
        venue.setId(id);
        venue.setVenueName(request.getVenueName());
        venue.setVenueAddress(request.getVenueAddress());
        venue.setContactPhone(request.getContactPhone());
        venue.setCategoryId(request.getCategoryId());
        venue.setDimensions(request.getDimensions());
        venue.setMaterial(request.getMaterial());
        venue.setCapacity(request.getCapacity());
        venue.setFacilities(request.getFacilities());
        venue.setFeeType(request.getFeeType());
        venue.setFeeAmount(request.getFeeAmount());
        venue.setCoverImage(request.getCoverImage());
        venue.setStatus(request.getStatus());
        venue.setRemark(request.getRemark());

        venueMapper.updateById(venue);
    }

    public void delete(Integer id) {
        Venue existing = venueMapper.selectById(id);
        if (existing == null) {
            throw new BusinessException("场地不存在");
        }
        int count = venueMapper.countBookingByVenueId(id);
        if (count > 0) {
            throw new BusinessException("该场地有预约记录，无法删除");
        }
        venueMapper.deleteById(id);
    }

    private String generateVenueCode() {
        return "V" + java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd"))
               + String.format("%03d", (int)(Math.random() * 1000));
    }
}
