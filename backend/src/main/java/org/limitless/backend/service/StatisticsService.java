package org.limitless.backend.service;

import org.limitless.backend.mapper.StatisticsMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class StatisticsService {

    private final StatisticsMapper statisticsMapper;

    public StatisticsService(StatisticsMapper statisticsMapper) {
        this.statisticsMapper = statisticsMapper;
    }

    public List<Map<String, Object>> getVenueUsage(String startDate, String endDate, Integer venueId) {
        return statisticsMapper.selectVenueUsage(startDate, endDate, venueId);
    }

    public List<Map<String, Object>> getActivityStats(String startDate, String endDate, Integer categoryId) {
        return statisticsMapper.selectActivityStats(startDate, endDate, categoryId);
    }
}
