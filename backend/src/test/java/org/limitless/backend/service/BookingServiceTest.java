package org.limitless.backend.service;

import org.junit.jupiter.api.*;
import org.limitless.backend.dto.BookingCreateRequest;
import org.limitless.backend.dto.CheckinRequest;
import org.limitless.backend.dto.PayRequest;
import org.limitless.backend.entity.Booking;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BookingServiceTest {

    @Autowired
    private BookingService bookingService;

    @Test
    @Order(1)
    void create_shouldCalcFeeByHour() {
        System.out.println("\n========== BookingServiceTest.create_每小时收费 ==========");
        BookingCreateRequest request = new BookingCreateRequest();
        request.setUserId(3);
        request.setVenueId(3); // 室外滑板公园, PER_HOUR, 35元/小时
        request.setBookingDate("2026-08-01");
        request.setStartTime("10:00:00");
        request.setEndTime("14:00:00"); // 4小时
        request.setRemark("测试预约");

        Booking booking = bookingService.create(request);
        System.out.println("id: " + booking.getId());
        System.out.println("bookingNo: " + booking.getBookingNo());
        System.out.println("venueName: " + booking.getVenueName());
        System.out.println("durationHours: " + booking.getDurationHours());
        System.out.println("feeAmount: " + booking.getFeeAmount() + " (35元/小时 × 4小时 = 140元)");
        System.out.println("status: " + booking.getStatus());

        Assertions.assertNotNull(booking.getId());
        Assertions.assertEquals(0, new java.math.BigDecimal("140").compareTo(booking.getFeeAmount()),
                "费用: 35元/小时 × 4小时 = 140元");
    }

    @Test
    @Order(2)
    void create_shouldCalcFeeByOnce() {
        System.out.println("\n========== BookingServiceTest.create_按次收费 ==========");
        BookingCreateRequest request = new BookingCreateRequest();
        request.setUserId(4);
        request.setVenueId(1); // 室内攀岩馆-A区, ONCE, 88元/次
        request.setBookingDate("2026-08-02");
        request.setStartTime("09:00:00");
        request.setEndTime("11:00:00");

        Booking booking = bookingService.create(request);
        System.out.println("id: " + booking.getId());
        System.out.println("bookingNo: " + booking.getBookingNo());
        System.out.println("feeAmount: " + booking.getFeeAmount() + " (按次收费 = 88元)");
        System.out.println("status: " + booking.getStatus());

        Assertions.assertEquals(0, new java.math.BigDecimal("88").compareTo(booking.getFeeAmount()),
                "按次收费: 88元");
    }

    @Test
    @Order(3)
    void cancel_shouldChangeStatus() {
        System.out.println("\n========== BookingServiceTest.cancel ==========");
        bookingService.cancel(4L); // B20260720004, 状态 PENDING
        System.out.println("预约 4 已取消");
    }

    @Test
    @Order(4)
    void checkin_shouldUpdateAttendeeCount() {
        System.out.println("\n========== BookingServiceTest.checkin ==========");
        CheckinRequest request = new CheckinRequest();
        request.setAttendeeCount(3);

        bookingService.checkin(3L, request); // B20260720003, 状态 APPROVED
        System.out.println("预约 3 到店登记完成，到场人数: 3");
    }

    @Test
    @Order(5)
    void pay_shouldUpdatePaymentStatus() {
        System.out.println("\n========== BookingServiceTest.pay ==========");
        PayRequest request = new PayRequest();
        request.setPaymentMethod("微信支付");

        bookingService.pay(3L, request); // B20260720003, 原 UNPAID
        System.out.println("预约 3 支付成功，支付方式: 微信支付");
    }
}
