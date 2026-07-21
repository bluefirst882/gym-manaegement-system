package org.limitless.backend.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.limitless.backend.common.BusinessException;
import org.limitless.backend.common.PageResult;
import org.limitless.backend.dto.ActivityCreateRequest;
import org.limitless.backend.dto.ActivityPageRequest;
import org.limitless.backend.dto.ActivityUpdateRequest;
import org.limitless.backend.entity.Activity;
import org.limitless.backend.mapper.ActivityMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ActivityService {

    private final ActivityMapper activityMapper;

    public ActivityService(ActivityMapper activityMapper) {
        this.activityMapper = activityMapper;
    }

    public PageResult<Activity> selectPage(ActivityPageRequest request) {
        PageHelper.startPage(request.getPageNumber(), request.getPageSize());
        List<Activity> list = activityMapper.selectPage(
                request.getTitle(), request.getCategoryId(), request.getStatus());
        PageInfo<Activity> pageInfo = new PageInfo<>(list);
        return new PageResult<>(pageInfo.getList(), request.getPageNumber(),
                request.getPageSize(), pageInfo.getTotal());
    }

    public Integer create(ActivityCreateRequest request) {
        if (request.getTitle() == null || request.getTitle().trim().isEmpty()) {
            throw new BusinessException("活动主题不能为空");
        }

        Activity activity = new Activity();
        activity.setActivityNo(generateActivityNo());
        activity.setTitle(request.getTitle());
        activity.setCoverImage(request.getCoverImage());
        activity.setCategoryId(request.getCategoryId());
        activity.setLocation(request.getLocation());
        if (request.getStartTime() != null) {
            activity.setStartTime(LocalDateTime.parse(request.getStartTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }
        if (request.getEndTime() != null) {
            activity.setEndTime(LocalDateTime.parse(request.getEndTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }
        activity.setStatus("DRAFT");
        activity.setRegistrationMethod(request.getRegistrationMethod() != null ? request.getRegistrationMethod() : "ONLINE");
        activity.setMaxParticipants(request.getMaxParticipants());
        activity.setAwards(request.getAwards());
        activity.setRules(request.getRules());
        activity.setDescription(request.getDescription());
        activity.setContact(request.getContact());
        activity.setFeeType(request.getFeeType() != null ? request.getFeeType() : "FREE");
        activity.setFeeAmount(request.getFeeAmount() != null ? request.getFeeAmount() : java.math.BigDecimal.ZERO);

        activityMapper.insert(activity);
        return activity.getId();
    }

    public void update(Integer id, ActivityUpdateRequest request) {
        Activity existing = activityMapper.selectById(id);
        if (existing == null) {
            throw new BusinessException("活动不存在");
        }

        Activity activity = new Activity();
        activity.setId(id);
        activity.setTitle(request.getTitle());
        activity.setCoverImage(request.getCoverImage());
        activity.setCategoryId(request.getCategoryId());
        activity.setLocation(request.getLocation());
        activity.setStatus(request.getStatus());
        activity.setMaxParticipants(request.getMaxParticipants());
        activity.setAwards(request.getAwards());
        activity.setRules(request.getRules());
        activity.setDescription(request.getDescription());
        activity.setContact(request.getContact());
        activity.setFeeType(request.getFeeType());
        activity.setFeeAmount(request.getFeeAmount());

        activityMapper.updateById(activity);
    }

    public void delete(Integer id) {
        Activity existing = activityMapper.selectById(id);
        if (existing == null) {
            throw new BusinessException("活动不存在");
        }
        activityMapper.deleteById(id);
    }

    private String generateActivityNo() {
        return "A" + java.time.LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
               + String.format("%03d", (int)(Math.random() * 1000));
    }
}
