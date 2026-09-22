package org.limitless.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.limitless.backend.entity.Booking;
import org.limitless.backend.entity.EquipmentRental;
import org.limitless.backend.mapper.BookingMapper;
import org.limitless.backend.mapper.EquipmentRentalMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

@SpringBootTest
@EnabledIfEnvironmentVariable(named = "GYM_MYSQL_TEST_URL", matches = ".+")
class MySqlConcurrencyTest {
    @Autowired BookingService bookingService;
    @Autowired EquipmentRentalService rentalService;
    @Autowired BookingMapper bookingMapper;
    @Autowired EquipmentRentalMapper rentalMapper;
    @Autowired JdbcTemplate jdbc;

    @DynamicPropertySource
    static void mysqlProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", () -> System.getenv("GYM_MYSQL_TEST_URL"));
        registry.add("spring.datasource.username", () -> System.getenv("GYM_MYSQL_TEST_USERNAME"));
        registry.add("spring.datasource.password", () -> System.getenv("GYM_MYSQL_TEST_PASSWORD"));
        registry.add("spring.datasource.driver-class-name", () -> "com.mysql.cj.jdbc.Driver");
        registry.add("spring.sql.init.mode", () -> "never");
    }

    @AfterEach
    void cleanup() {
        jdbc.update("DELETE FROM booking WHERE remark = 'mysql-concurrency-test'");
        jdbc.update("DELETE FROM equipment_rental WHERE borrower = 'mysql-concurrency-test'");
    }

    @Test
    void overlappingBookingsOnlyOneCanCommit() throws Exception {
        String date = LocalDate.now().plusDays(30).toString();
        var gate = new CountDownLatch(1);
        var pool = Executors.newFixedThreadPool(2);
        Future<Boolean> first = pool.submit(() -> createBookingAfterGate(gate, date));
        Future<Boolean> second = pool.submit(() -> createBookingAfterGate(gate, date));
        gate.countDown();

        int success = (first.get() ? 1 : 0) + (second.get() ? 1 : 0);
        pool.shutdownNow();
        assertEquals(1, success);
        assertEquals(1, jdbc.queryForObject(
                "SELECT COUNT(*) FROM booking WHERE booking_date = ? AND venue_id = 3 AND remark = 'mysql-concurrency-test'",
                Integer.class, date));
    }

    @Test
    void concurrentReturnRestoresStockOnlyOnce() throws Exception {
        EquipmentRental rental = new EquipmentRental();
        rental.setEquipmentId(2);
        rental.setQuantity(1);
        rental.setStartTime(LocalDateTime.now());
        rental.setEndTime(LocalDateTime.now().plusHours(2));
        rental.setBorrower("mysql-concurrency-test");
        rental.setContactPhone("13800000000");
        rentalService.create(rental);
        int before = jdbc.queryForObject("SELECT available_quantity FROM equipment WHERE id = 2", Integer.class);

        var gate = new CountDownLatch(1);
        var pool = Executors.newFixedThreadPool(2);
        Future<Boolean> first = pool.submit(() -> returnAfterGate(gate, rental.getId()));
        Future<Boolean> second = pool.submit(() -> returnAfterGate(gate, rental.getId()));
        gate.countDown();
        int success = (first.get() ? 1 : 0) + (second.get() ? 1 : 0);
        pool.shutdownNow();

        assertEquals(1, success);
        assertEquals(before + 1, jdbc.queryForObject("SELECT available_quantity FROM equipment WHERE id = 2", Integer.class));
        assertEquals("RETURNED", rentalMapper.selectById(rental.getId()).getStatus());
    }

    private boolean createBookingAfterGate(CountDownLatch gate, String date) throws InterruptedException {
        gate.await();
        try {
            var request = new org.limitless.backend.dto.BookingCreateRequest();
            request.setUserId(3);
            request.setVenueId(3);
            request.setBookingDate(date);
            request.setStartTime("10:00:00");
            request.setEndTime("12:00:00");
            request.setRemark("mysql-concurrency-test");
            bookingService.create(request);
            return true;
        } catch (RuntimeException expected) {
            return false;
        }
    }

    private boolean returnAfterGate(CountDownLatch gate, Integer rentalId) throws InterruptedException {
        gate.await();
        try {
            rentalService.returnEquipment(rentalId, null);
            return true;
        } catch (RuntimeException expected) {
            return false;
        }
    }
}
