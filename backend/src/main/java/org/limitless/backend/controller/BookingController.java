package org.limitless.backend.controller;

import org.limitless.backend.common.PageResult;
import org.limitless.backend.common.Result;
import org.limitless.backend.dto.*;
import org.limitless.backend.entity.Booking;
import org.limitless.backend.service.BookingService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.servlet.http.HttpServletRequest;
import org.limitless.backend.common.BusinessException;

@RestController
@RequestMapping("/api")
public class BookingController {

    private final BookingService bookingService;
    private final org.limitless.backend.service.RedisRequestGuard requestGuard;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
        this.requestGuard = new org.limitless.backend.service.RedisRequestGuard();
    }

    @Autowired
    public BookingController(BookingService bookingService,
                             org.limitless.backend.service.RedisRequestGuard requestGuard) {
        this.bookingService = bookingService;
        this.requestGuard = requestGuard;
    }

    /**
     * 分页查询预约
     * POST /api/bookings
     */
    @PostMapping("/bookings")
    public Result<PageResult<Booking>> listBookings(@RequestBody BookingPageRequest request, HttpServletRequest httpRequest) {
        if (!isAdmin(httpRequest)) {
            request.setUserId(currentUserId(httpRequest));
        }
        PageResult<Booking> result = bookingService.selectPage(request);
        return Result.success("查询成功", result);
    }

    /**
     * 新增预约
     * POST /api/booking
     */
    @PostMapping("/booking")
    public Result<Object> createBooking(@RequestBody BookingCreateRequest request, HttpServletRequest httpRequest) {
        if (!isAdmin(httpRequest)) {
            request.setUserId(currentUserId(httpRequest));
        }
        Integer userId = currentUserId(httpRequest);
        requestGuard.checkRate("booking-create", userId);
        String requestId = httpRequest.getHeader("Idempotency-Key");
        var decision = requestGuard.begin("booking-create", userId, requestId);
        if (!decision.acquired()) return bookingResult(bookingService.selectById(Long.valueOf(decision.resultId())));
        Booking booking;
        try {
            booking = bookingService.create(request);
            requestGuard.complete("booking-create", userId, requestId, String.valueOf(booking.getId()));
        } catch (RuntimeException ex) {
            requestGuard.release("booking-create", userId, requestId);
            throw ex;
        }
        return bookingResult(booking);
    }

    private Result<Object> bookingResult(Booking booking) {
        return Result.success("预约提交成功", new java.util.HashMap<String, Object>() {{
            put("id", booking.getId());
            put("bookingNo", booking.getBookingNo());
            put("status", booking.getStatus());
            put("feeAmount", booking.getFeeAmount());
        }});
    }

    /**
     * 取消预约
     * PUT /api/booking/{id}/cancel
     */
    @PutMapping("/booking/{id}/cancel")
    public Result<Void> cancelBooking(@PathVariable Long id, HttpServletRequest httpRequest) {
        requireOwnerOrAdmin(id, httpRequest);
        bookingService.cancel(id);
        return Result.success("预约已取消");
    }

    /**
     * 管理员审核预约
     * PUT /api/booking/{id}/audit
     */
    @PutMapping("/booking/{id}/audit")
    public Result<Void> auditBooking(@PathVariable Long id, @RequestBody BookingAuditRequest request, HttpServletRequest httpRequest) {
        requireAdmin(httpRequest);
        bookingService.audit(id, request);
        return Result.success("审核成功");
    }

    /**
     * 删除预约
     * DELETE /api/booking/{id}
     */
    @DeleteMapping("/booking/{id}")
    public Result<Void> deleteBooking(@PathVariable Long id, HttpServletRequest httpRequest) {
        requireOwnerOrAdmin(id, httpRequest);
        bookingService.delete(id);
        return Result.success("删除成功");
    }

    /**
     * 到店登记
     * PUT /api/booking/{id}/checkin
     */
    @PutMapping("/booking/{id}/checkin")
    public Result<Void> checkin(@PathVariable Long id, @RequestBody CheckinRequest request, HttpServletRequest httpRequest) {
        requireOwnerOrAdmin(id, httpRequest);
        bookingService.checkin(id, request);
        return Result.success("到店登记成功");
    }

    /**
     * 离店
     * PUT /api/booking/{id}/checkout
     */
    @PutMapping("/booking/{id}/checkout")
    public Result<Object> checkout(@PathVariable Long id, @RequestBody CheckoutRequest request, HttpServletRequest httpRequest) {
        requireOwnerOrAdmin(id, httpRequest);
        Object result = bookingService.checkout(id, request);
        return Result.success("离店登记成功", result);
    }

    /**
     * 支付
     * PUT /api/booking/{id}/pay
     */
    @PutMapping("/booking/{id}/pay")
    public Result<Void> pay(@PathVariable Long id, @RequestBody PayRequest request, HttpServletRequest httpRequest) {
        requireOwnerOrAdmin(id, httpRequest);
        Integer userId = currentUserId(httpRequest);
        requestGuard.checkRate("booking-pay", userId);
        String requestId = httpRequest.getHeader("Idempotency-Key");
        var decision = requestGuard.begin("booking-pay-" + id, userId, requestId);
        if (decision.acquired()) {
            try {
                bookingService.pay(id, request);
                requestGuard.complete("booking-pay-" + id, userId, requestId, String.valueOf(id));
            } catch (RuntimeException ex) {
                requestGuard.release("booking-pay-" + id, userId, requestId);
                throw ex;
            }
        }
        return Result.success("支付成功");
    }

    /**
     * 修改缴费状态
     * PUT /api/booking/{id}/payment-status
     */
    @PutMapping("/booking/{id}/payment-status")
    public Result<Void> updatePaymentStatus(@PathVariable Long id, @RequestBody PaymentStatusRequest request, HttpServletRequest httpRequest) {
        requireAdmin(httpRequest);
        bookingService.updatePaymentStatus(id, request);
        return Result.success("缴费状态更新成功");
    }

    private Integer currentUserId(HttpServletRequest request) {
        Integer userId = (Integer) request.getAttribute("currentUserId");
        if (userId == null) throw new BusinessException(401, "未登录");
        return userId;
    }

    private boolean isAdmin(HttpServletRequest request) {
        return "ADMIN".equals(request.getAttribute("currentRoleCode"))
                || Integer.valueOf(1).equals(request.getAttribute("currentRoleId"));
    }

    private void requireAdmin(HttpServletRequest request) {
        currentUserId(request);
        if (!isAdmin(request)) throw new BusinessException(403, "仅管理员可以执行此操作");
    }

    private void requireOwnerOrAdmin(Long id, HttpServletRequest request) {
        if (isAdmin(request)) return;
        Integer userId = currentUserId(request);
        Booking booking = bookingService.selectById(id);
        if (booking == null) throw new BusinessException("预约记录不存在");
        if (!userId.equals(booking.getUserId())) throw new BusinessException(403, "不能操作他人的预约");
    }
}
