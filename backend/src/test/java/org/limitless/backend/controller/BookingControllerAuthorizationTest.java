package org.limitless.backend.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.limitless.backend.common.BusinessException;
import org.limitless.backend.dto.BookingAuditRequest;
import org.limitless.backend.dto.BookingCreateRequest;
import org.limitless.backend.entity.Booking;
import org.limitless.backend.service.BookingService;
import org.mockito.ArgumentCaptor;
import org.springframework.mock.web.MockHttpServletRequest;

class BookingControllerAuthorizationTest {
    private final BookingService service = mock(BookingService.class);
    private final BookingController controller = new BookingController(service);

    @Test
    void ordinaryUserCannotChooseAnotherUserWhenCreating() {
        Booking created = new Booking();
        created.setId(1L);
        created.setBookingNo("BTEST");
        when(service.create(any())).thenReturn(created);
        BookingCreateRequest request = new BookingCreateRequest();
        request.setUserId(999);
        request.setVenueId(3);
        request.setBookingDate("2026-12-01");
        request.setStartTime("10:00:00");
        request.setEndTime("11:00:00");

        controller.createBooking(request, userRequest(7, 2));

        ArgumentCaptor<BookingCreateRequest> captured = ArgumentCaptor.forClass(BookingCreateRequest.class);
        verify(service).create(captured.capture());
        assertEquals(7, captured.getValue().getUserId());
    }

    @Test
    void ordinaryUserCannotAudit() {
        assertThrows(BusinessException.class, () -> controller.auditBooking(1L, new BookingAuditRequest(), userRequest(7, 2)));
        verifyNoInteractions(service);
    }

    private HttpServletRequest userRequest(int userId, int roleId) {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setAttribute("currentUserId", userId);
        request.setAttribute("currentRoleId", roleId);
        return request;
    }
}
