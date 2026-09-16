package org.limitless.backend.controller;

import org.limitless.backend.common.Result;
import org.limitless.backend.service.StatisticsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    private final StatisticsService statisticsService;

    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    /**
     * 场地使用率统计
     * POST /api/statistics/venue-usage
     */
    @PostMapping("/venue-usage")
    public Result<List<Map<String, Object>>> venueUsage(@RequestBody Map<String, Object> params) {
        String startDate = (String) params.get("startDate");
        String endDate = (String) params.get("endDate");
        Integer venueId = (Integer) params.get("venueId");
        List<Map<String, Object>> result = statisticsService.getVenueUsage(startDate, endDate, venueId);
        return Result.success("查询成功", result);
    }

    /**
     * 活动统计
     * POST /api/statistics/activity
     */
    @PostMapping("/activity")
    public Result<List<Map<String, Object>>> activityStats(@RequestBody Map<String, Object> params) {
        String startDate = (String) params.get("startDate");
        String endDate = (String) params.get("endDate");
        Integer categoryId = (Integer) params.get("categoryId");
        List<Map<String, Object>> result = statisticsService.getActivityStats(startDate, endDate, categoryId);
        return Result.success("查询成功", result);
    }
}
