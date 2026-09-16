package org.limitless.backend.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.limitless.backend.common.BusinessException;
import org.limitless.backend.common.PageResult;
import org.limitless.backend.dto.AuditRequest;
import org.limitless.backend.dto.RegistrationCreateRequest;
import org.limitless.backend.dto.RegistrationPageRequest;
import org.limitless.backend.entity.ActivityRegistration;
import org.limitless.backend.mapper.ActivityRegistrationMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ActivityRegistrationService {

    private final ActivityRegistrationMapper registrationMapper;

    public ActivityRegistrationService(ActivityRegistrationMapper registrationMapper) {
        this.registrationMapper = registrationMapper;
    }

    public PageResult<ActivityRegistration> selectPage(RegistrationPageRequest request) {
        PageHelper.startPage(request.getPageNumber(), request.getPageSize());
        List<ActivityRegistration> list = registrationMapper.selectPage(
                request.getActivityId(), request.getActivityTitle(), request.getUserId(),
                request.getAuditStatus(), request.getRegStartTime(), request.getRegEndTime());
        PageInfo<ActivityRegistration> pageInfo = new PageInfo<>(list);
        return new PageResult<>(pageInfo.getList(), request.getPageNumber(),
                request.getPageSize(), pageInfo.getTotal());
    }

    public ActivityRegistration create(RegistrationCreateRequest request) {
        if (request.getActivityId() == null) {
            throw new BusinessException("活动ID不能为空");
        }
        if (request.getUserId() == null) {
            throw new BusinessException("用户ID不能为空");
        }

        ActivityRegistration registration = new ActivityRegistration();
        registration.setRegistrationNo(generateRegistrationNo());
        registration.setActivityId(request.getActivityId());
        registration.setUserId(request.getUserId());
        registration.setActivityType(request.getActivityType());
        registration.setRemark(request.getRemark());
        registration.setAuditStatus("PENDING");

        registrationMapper.insert(registration);
        return registration;
    }

    public void audit(Long id, AuditRequest request) {
        ActivityRegistration registration = registrationMapper.selectById(id);
        if (registration == null) {
            throw new BusinessException("报名记录不存在");
        }
        if (!"PENDING".equals(registration.getAuditStatus())) {
            throw new BusinessException("该报名已审核，不能重复审核");
        }

        ActivityRegistration update = new ActivityRegistration();
        update.setId(id);
        update.setAuditStatus(request.getAuditStatus());
        update.setAuditComment(request.getAuditComment());
        update.setAuditTime(LocalDateTime.now());
        registrationMapper.updateById(update);
    }

    public void cancel(Long id) {
        ActivityRegistration registration = registrationMapper.selectById(id);
        if (registration == null) {
            throw new BusinessException("报名记录不存在");
        }
        if (!"PENDING".equals(registration.getAuditStatus())) {
            throw new BusinessException("仅待审核报名可以取消");
        }
        ActivityRegistration update = new ActivityRegistration();
        update.setId(id);
        update.setAuditStatus("CANCELLED");
        registrationMapper.updateById(update);
    }

    public void delete(Long id) {
        ActivityRegistration existing = registrationMapper.selectById(id);
        if (existing == null) {
            throw new BusinessException("报名记录不存在");
        }
        registrationMapper.deleteById(id);
    }

    private String generateRegistrationNo() {
        return "REG" + java.time.LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
               + String.format("%03d", (int)(Math.random() * 1000));
    }
}
