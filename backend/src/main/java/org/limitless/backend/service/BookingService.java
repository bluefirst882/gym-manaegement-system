package org.limitless.backend.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.limitless.backend.common.BusinessException;
import org.limitless.backend.common.PageResult;
import org.limitless.backend.dto.*;
import org.limitless.backend.entity.Booking;
import org.limitless.backend.entity.Venue;
import org.limitless.backend.mapper.BookingMapper;
import org.limitless.backend.mapper.VenueMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class BookingService {

    private final BookingMapper bookingMapper;
    private final VenueMapper venueMapper;

    public BookingService(BookingMapper bookingMapper, VenueMapper venueMapper) {
        this.bookingMapper = bookingMapper;
        this.venueMapper = venueMapper;
    }

    public PageResult<Booking> selectPage(BookingPageRequest request) {
        PageHelper.startPage(request.getPageNumber(), request.getPageSize());
        List<Booking> list = bookingMapper.selectPage(
                request.getBookingNo(), request.getUserId(), request.getVenueId(),
                request.getStatus(), request.getPaymentStatus(),
                request.getBookingDateStart(), request.getBookingDateEnd());
        PageInfo<Booking> pageInfo = new PageInfo<>(list);
        return new PageResult<>(pageInfo.getList(), request.getPageNumber(),
                request.getPageSize(), pageInfo.getTotal());
    }

    public Booking selectById(Long id) {
        return bookingMapper.selectById(id);
    }

    @Transactional
    public Booking create(BookingCreateRequest request) {
        if (request.getUserId() == null) {
            throw new BusinessException("用户ID不能为空");
        }
        if (request.getVenueId() == null) {
            throw new BusinessException("场地ID不能为空");
        }

        // 串行化同一场地的预约创建，避免并发请求同时通过“先查后插”。
        Venue venue = venueMapper.lockForBooking(request.getVenueId());
        if (venue == null) {
            throw new BusinessException("场地不存在");
        }
        if (!"AVAILABLE".equals(venue.getStatus())) {
            throw new BusinessException("场地当前不可用");
        }

        LocalDate bookingDate = LocalDate.parse(request.getBookingDate(), DateTimeFormatter.ISO_LOCAL_DATE);
        LocalTime startTime = LocalTime.parse(request.getStartTime(), DateTimeFormatter.ofPattern("HH:mm:ss"));
        LocalTime endTime = LocalTime.parse(request.getEndTime(), DateTimeFormatter.ofPattern("HH:mm:ss"));

        if (!startTime.isBefore(endTime)) {
            throw new BusinessException("开始时间必须早于结束时间");
        }

        // 检查时间冲突
        int conflict = bookingMapper.checkTimeConflict(
                request.getVenueId(), request.getBookingDate(),
                request.getStartTime(), request.getEndTime(), null);
        if (conflict > 0) {
            throw new BusinessException("该时段已被预约，请选择其他时段");
        }

        // 计算时长
        long minutes = ChronoUnit.MINUTES.between(startTime, endTime);
        BigDecimal hours = BigDecimal.valueOf(minutes).divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP);

        // 计算费用
        BigDecimal feeAmount = BigDecimal.ZERO;
        if ("ONCE".equals(venue.getFeeType())) {
            feeAmount = venue.getFeeAmount();
        } else if ("PER_HOUR".equals(venue.getFeeType())) {
            feeAmount = venue.getFeeAmount().multiply(hours).setScale(2, RoundingMode.HALF_UP);
        }

        Booking booking = new Booking();
        booking.setBookingNo(generateBookingNo());
        booking.setUserId(request.getUserId());
        booking.setVenueId(request.getVenueId());
        booking.setVenueName(venue.getVenueName());
        booking.setVenueAddress(venue.getVenueAddress());
        booking.setBookingDate(bookingDate);
        booking.setStartTime(startTime);
        booking.setEndTime(endTime);
        booking.setDurationHours(hours);
        booking.setRemark(request.getRemark());
        booking.setStatus("PENDING");
        booking.setSource("BACKEND");
        booking.setFeeAmount(feeAmount);
        booking.setPaymentStatus("UNPAID");

        bookingMapper.insert(booking);
        return booking;
    }

    public void cancel(Long id) {
        Booking booking = bookingMapper.selectById(id);
        if (booking == null) {
            throw new BusinessException("预约记录不存在");
        }
        if (!"PENDING".equals(booking.getStatus()) && !"APPROVED".equals(booking.getStatus())) {
            throw new BusinessException("当前状态不允许取消");
        }
        if (bookingMapper.updateStatusIfCurrent(id, booking.getStatus(), "CANCELLED") != 1) {
            throw new BusinessException("预约状态已发生变化，请刷新后重试");
        }
    }

    public void audit(Long id, BookingAuditRequest request) {
        Booking booking = bookingMapper.selectById(id);
        if (booking == null) {
            throw new BusinessException("预约记录不存在");
        }
        if (!"PENDING".equals(booking.getStatus())) {
            throw new BusinessException("仅待审核预约可以审核");
        }
        if (!"APPROVED".equals(request.getStatus()) && !"REJECTED".equals(request.getStatus())) {
            throw new BusinessException("审核状态必须为通过或拒绝");
        }
        if (bookingMapper.updateStatusIfCurrent(id, "PENDING", request.getStatus()) != 1) {
            throw new BusinessException("预约状态已发生变化，请刷新后重试");
        }
    }

    public void delete(Long id) {
        Booking booking = bookingMapper.selectById(id);
        if (booking == null) {
            throw new BusinessException("预约记录不存在");
        }
        bookingMapper.deleteById(id);
    }

    public void checkin(Long id, CheckinRequest request) {
        Booking booking = bookingMapper.selectById(id);
        if (booking == null) {
            throw new BusinessException("预约记录不存在");
        }
        if (!"APPROVED".equals(booking.getStatus()) || booking.getCheckinTime() != null
                || booking.getCheckoutTime() != null) {
            throw new BusinessException("只有已通过的预约才能到店登记");
        }

        Booking update = new Booking();
        update.setId(id);
        update.setCheckinTime(request.getCheckinTime() != null ?
                LocalDateTime.parse(request.getCheckinTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) :
                LocalDateTime.now());
        update.setAttendeeCount(request.getAttendeeCount());
        if (bookingMapper.updateById(update) != 1) {
            throw new BusinessException("到店登记失败，请刷新后重试");
        }
    }

    public Object checkout(Long id, CheckoutRequest request) {
        Booking booking = bookingMapper.selectById(id);
        if (booking == null) {
            throw new BusinessException("预约记录不存在");
        }

        if (!"APPROVED".equals(booking.getStatus()) || booking.getCheckinTime() == null
                || booking.getCheckoutTime() != null) {
            throw new BusinessException("只有已到店且未离店的预约才能离店");
        }
        Booking update = new Booking();
        update.setId(id);
        update.setCheckoutTime(request.getCheckoutTime() != null ?
                LocalDateTime.parse(request.getCheckoutTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) :
                LocalDateTime.now());
        update.setStatus("COMPLETED");
        if (bookingMapper.updateById(update) != 1) {
            throw new BusinessException("离店登记失败，请刷新后重试");
        }

        Venue venue = venueMapper.selectById(booking.getVenueId());
        String feeDetail;
        if ("ONCE".equals(venue.getFeeType())) {
            feeDetail = "该场地按次收费，费用" + booking.getFeeAmount() + "元";
        } else {
            feeDetail = "该场地按小时收费，费用" + booking.getFeeAmount() + "元";
        }

        return new java.util.HashMap<String, Object>() {{
            put("feeAmount", booking.getFeeAmount());
            put("feeDetail", feeDetail);
        }};
    }

    public void pay(Long id, PayRequest request) {
        Booking booking = bookingMapper.selectById(id);
        if (booking == null) {
            throw new BusinessException("预约记录不存在");
        }
        if ("PAID".equals(booking.getPaymentStatus())) {
            throw new BusinessException("该预约已支付");
        }

        if (!"PENDING".equals(booking.getStatus()) && !"APPROVED".equals(booking.getStatus())) {
            throw new BusinessException("当前状态不允许支付");
        }
        Booking update = new Booking();
        update.setId(id);
        update.setPaymentStatus("PAID");
        update.setPaymentMethod(request.getPaymentMethod());
        if (bookingMapper.updateById(update) != 1) {
            throw new BusinessException("支付状态更新失败，请刷新后重试");
        }
    }

    public void updatePaymentStatus(Long id, PaymentStatusRequest request) {
        Booking booking = bookingMapper.selectById(id);
        if (booking == null) {
            throw new BusinessException("预约记录不存在");
        }

        if (!List.of("UNPAID", "PAID", "REFUNDED").contains(request.getPaymentStatus())) {
            throw new BusinessException("缴费状态不合法");
        }
        if (request.getAmount() != null && request.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("金额不能为负数");
        }
        Booking update = new Booking();
        update.setId(id);
        update.setPaymentStatus(request.getPaymentStatus());
        update.setPaymentMethod(request.getPaymentMethod());
        if (request.getAmount() != null) {
            update.setFeeAmount(request.getAmount());
        }
        if (bookingMapper.updateById(update) != 1) {
            throw new BusinessException("缴费状态更新失败，请刷新后重试");
        }
    }

    private String generateBookingNo() {
        return "B" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
               + java.util.UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase();
    }
}
