package org.limitless.backend.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.limitless.backend.entity.Booking;

import java.util.List;

@Mapper
public interface BookingMapper {
    Booking selectById(Long id);

    List<Booking> selectPage(@Param("bookingNo") String bookingNo,
                             @Param("userId") Integer userId,
                             @Param("venueId") Integer venueId,
                             @Param("status") String status,
                             @Param("paymentStatus") String paymentStatus,
                             @Param("bookingDateStart") String bookingDateStart,
                             @Param("bookingDateEnd") String bookingDateEnd);

    int insert(Booking booking);

    int updateById(Booking booking);

    int deleteById(Long id);

    int checkTimeConflict(@Param("venueId") Integer venueId,
                          @Param("bookingDate") String bookingDate,
                          @Param("startTime") String startTime,
                          @Param("endTime") String endTime,
                          @Param("excludeId") Long excludeId);
}
