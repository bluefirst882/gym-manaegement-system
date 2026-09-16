package org.limitless.backend.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.limitless.backend.entity.Venue;

import java.util.List;

@Mapper
public interface VenueMapper {
    Venue selectById(Integer id);

    List<Venue> selectPage(@Param("venueName") String venueName,
                           @Param("venueAddress") String venueAddress,
                           @Param("categoryId") Integer categoryId,
                           @Param("status") String status);

    long countPage(@Param("venueName") String venueName,
                   @Param("venueAddress") String venueAddress,
                   @Param("categoryId") Integer categoryId,
                   @Param("status") String status);

    int insert(Venue venue);

    int updateById(Venue venue);

    int deleteById(Integer id);

    int countBookingByVenueId(Integer venueId);
}
