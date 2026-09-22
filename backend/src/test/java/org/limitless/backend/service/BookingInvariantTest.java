package org.limitless.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.limitless.backend.common.BusinessException;
import org.limitless.backend.dto.BookingCreateRequest;
import org.limitless.backend.dto.CheckoutRequest;
import org.limitless.backend.entity.Booking;
import org.limitless.backend.entity.Venue;
import org.limitless.backend.mapper.BookingMapper;
import org.limitless.backend.mapper.VenueMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BookingInvariantTest {
    @Mock BookingMapper bookingMapper;
    @Mock VenueMapper venueMapper;
    @InjectMocks BookingService service;

    @Test
    void createUsesDecimalDurationAndLocksVenue() {
        Venue venue = venue(35, "PER_HOUR");
        when(venueMapper.lockForBooking(3)).thenReturn(venue);
        when(bookingMapper.checkTimeConflict(3, "2026-08-01", "10:00:00", "11:30:00", null)).thenReturn(0);

        BookingCreateRequest request = new BookingCreateRequest();
        request.setUserId(4);
        request.setVenueId(3);
        request.setBookingDate("2026-08-01");
        request.setStartTime("10:00:00");
        request.setEndTime("11:30:00");

        Booking result = service.create(request);

        assertEquals(new BigDecimal("1.50"), result.getDurationHours());
        assertEquals(new BigDecimal("52.50"), result.getFeeAmount());
        verify(venueMapper).lockForBooking(3);
        verify(bookingMapper).insert(any(Booking.class));
    }

    @Test
    void checkoutRequiresSuccessfulCheckin() {
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setStatus("APPROVED");
        booking.setCheckinTime(null);
        when(bookingMapper.selectById(1L)).thenReturn(booking);

        assertThrows(BusinessException.class, () -> service.checkout(1L, new CheckoutRequest()));
        verify(bookingMapper, never()).updateById(any(Booking.class));
    }

    private Venue venue(int amount, String feeType) {
        Venue venue = new Venue();
        venue.setId(3);
        venue.setVenueName("测试场地");
        venue.setVenueAddress("测试地址");
        venue.setStatus("AVAILABLE");
        venue.setFeeType(feeType);
        venue.setFeeAmount(BigDecimal.valueOf(amount));
        return venue;
    }
}
