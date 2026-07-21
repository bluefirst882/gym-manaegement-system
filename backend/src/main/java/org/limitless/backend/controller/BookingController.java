package org.limitless.backend.controller;

import org.limitless.backend.common.PageResult;
import org.limitless.backend.common.Result;
import org.limitless.backend.dto.*;
import org.limitless.backend.entity.Booking;
import org.limitless.backend.service.BookingService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    /**
     * 分页查询预约
     * POST /api/bookings
     */
    @PostMapping("/bookings")
    public Result<PageResult<Booking>> listBookings(@RequestBody BookingPageRequest request) {
        PageResult<Booking> result = bookingService.selectPage(request);
        return Result.success("查询成功", result);
    }

    /**
     * 新增预约
     * POST /api/booking
     */
    @PostMapping("/booking")
    public Result<Object> createBooking(@RequestBody BookingCreateRequest request) {
        Booking booking = bookingService.create(request);
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
    public Result<Void> cancelBooking(@PathVariable Long id) {
        bookingService.cancel(id);
        return Result.success("预约已取消");
    }

    /**
     * 管理员审核预约
     * PUT /api/booking/{id}/audit
     */
    @PutMapping("/booking/{id}/audit")
    public Result<Void> auditBooking(@PathVariable Long id, @RequestBody BookingAuditRequest request) {
        bookingService.audit(id, request);
        return Result.success("审核成功");
    }

    /**
     * 删除预约
     * DELETE /api/booking/{id}
     */
    @DeleteMapping("/booking/{id}")
    public Result<Void> deleteBooking(@PathVariable Long id) {
        bookingService.delete(id);
        return Result.success("删除成功");
    }

    /**
     * 到店登记
     * PUT /api/booking/{id}/checkin
     */
    @PutMapping("/booking/{id}/checkin")
    public Result<Void> checkin(@PathVariable Long id, @RequestBody CheckinRequest request) {
        bookingService.checkin(id, request);
        return Result.success("到店登记成功");
    }

    /**
     * 离店
     * PUT /api/booking/{id}/checkout
     */
    @PutMapping("/booking/{id}/checkout")
    public Result<Object> checkout(@PathVariable Long id, @RequestBody CheckoutRequest request) {
        Object result = bookingService.checkout(id, request);
        return Result.success("离店登记成功", result);
    }

    /**
     * 支付
     * PUT /api/booking/{id}/pay
     */
    @PutMapping("/booking/{id}/pay")
    public Result<Void> pay(@PathVariable Long id, @RequestBody PayRequest request) {
        bookingService.pay(id, request);
        return Result.success("支付成功");
    }

    /**
     * 修改缴费状态
     * PUT /api/booking/{id}/payment-status
     */
    @PutMapping("/booking/{id}/payment-status")
    public Result<Void> updatePaymentStatus(@PathVariable Long id, @RequestBody PaymentStatusRequest request) {
        bookingService.updatePaymentStatus(id, request);
        return Result.success("缴费状态更新成功");
    }
}
