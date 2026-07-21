package org.limitless.backend.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

@Mapper
public interface StatisticsMapper {

    List<Map<String, Object>> selectVenueUsage(@Param("startDate") String startDate,
                                                @Param("endDate") String endDate,
                                                @Param("venueId") Integer venueId);

    List<Map<String, Object>> selectActivityStats(@Param("startDate") String startDate,
                                                   @Param("endDate") String endDate,
                                                   @Param("categoryId") Integer categoryId);
}
