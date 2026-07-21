package org.limitless.backend.controller;

import org.limitless.backend.common.PageResult;
import org.limitless.backend.common.Result;
import org.limitless.backend.dto.VenueCreateRequest;
import org.limitless.backend.dto.VenuePageRequest;
import org.limitless.backend.dto.VenueUpdateRequest;
import org.limitless.backend.entity.Venue;
import org.limitless.backend.service.VenueService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class VenueController {

    private final VenueService venueService;

    public VenueController(VenueService venueService) {
        this.venueService = venueService;
    }

    /**
     * 分页查询场地
     * POST /api/venues
     */
    @PostMapping("/venues")
    public Result<PageResult<Venue>> listVenues(@RequestBody VenuePageRequest request) {
        PageResult<Venue> result = venueService.selectPage(request);
        return Result.success("查询成功", result);
    }

    /**
     * 查询场地详情
     * GET /api/venue/{id}
     */
    @GetMapping("/venue/{id}")
    public Result<Venue> getVenue(@PathVariable Integer id) {
        Venue venue = venueService.selectById(id);
        return Result.success("查询成功", venue);
    }

    /**
     * 新增场地
     * POST /api/venue
     */
    @PostMapping("/venue")
    public Result<Integer> createVenue(@RequestBody VenueCreateRequest request) {
        Integer id = venueService.create(request);
        return Result.success("新增成功", id);
    }

    /**
     * 修改场地
     * PUT /api/venue/{id}
     */
    @PutMapping("/venue/{id}")
    public Result<Void> updateVenue(@PathVariable Integer id, @RequestBody VenueUpdateRequest request) {
        venueService.update(id, request);
        return Result.success("修改成功");
    }

    /**
     * 删除场地
     * DELETE /api/venue/{id}
     */
    @DeleteMapping("/venue/{id}")
    public Result<Void> deleteVenue(@PathVariable Integer id) {
        venueService.delete(id);
        return Result.success("删除成功");
    }
}
