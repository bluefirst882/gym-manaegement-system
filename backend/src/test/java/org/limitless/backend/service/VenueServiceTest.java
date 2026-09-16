package org.limitless.backend.service;

import org.junit.jupiter.api.*;
import org.limitless.backend.common.BusinessException;
import org.limitless.backend.entity.Venue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class VenueServiceTest {

    @Autowired
    private VenueService venueService;

    @Test
    @Order(1)
    void selectById_shouldReturnVenue() {
        System.out.println("\n========== VenueServiceTest.selectById ==========");
        Venue venue = venueService.selectById(1);
        System.out.println("id: " + venue.getId());
        System.out.println("venueCode: " + venue.getVenueCode());
        System.out.println("venueName: " + venue.getVenueName());
        System.out.println("venueAddress: " + venue.getVenueAddress());
        System.out.println("categoryName: " + venue.getCategoryName());
        System.out.println("feeType: " + venue.getFeeType());
        System.out.println("feeAmount: " + venue.getFeeAmount());
        System.out.println("status: " + venue.getStatus());

        Assertions.assertEquals("室内攀岩馆-A区", venue.getVenueName());
        Assertions.assertNotNull(venue.getCategoryName(), "分类名不应为空");
    }

    @Test
    @Order(2)
    void delete_shouldFailWhenBookingExists() {
        System.out.println("\n========== VenueServiceTest.delete_shouldFail ==========");
        Assertions.assertThrows(BusinessException.class, () -> venueService.delete(1));
        System.out.println("正确：场地 1 有预约记录，禁止删除");
    }
}
