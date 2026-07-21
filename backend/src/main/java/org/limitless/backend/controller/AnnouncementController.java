package org.limitless.backend.controller;

import org.limitless.backend.common.PageResult;
import org.limitless.backend.common.Result;
import org.limitless.backend.entity.Announcement;
import org.limitless.backend.service.AnnouncementService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class AnnouncementController {

    private final AnnouncementService announcementService;

    public AnnouncementController(AnnouncementService announcementService) {
        this.announcementService = announcementService;
    }

    @PostMapping("/announcements")
    public Result<PageResult<Announcement>> listAnnouncements(@RequestBody Map<String, Object> params) {
        int pageNumber = params.get("pageNumber") != null ? (int) params.get("pageNumber") : 1;
        int pageSize = params.get("pageSize") != null ? (int) params.get("pageSize") : 10;
        String title = (String) params.get("title");
        String status = (String) params.get("status");
        String startTime = (String) params.get("startTime");
        String endTime = (String) params.get("endTime");
        PageResult<Announcement> result = announcementService.selectPage(title, status, startTime, endTime, pageNumber, pageSize);
        return Result.success("查询成功", result);
    }

    @GetMapping("/announcement/{id}")
    public Result<Announcement> getAnnouncement(@PathVariable Integer id) {
        Announcement announcement = announcementService.selectById(id);
        return Result.success("查询成功", announcement);
    }

    @PostMapping("/announcement")
    public Result<Integer> createAnnouncement(@RequestBody Announcement announcement) {
        Integer id = announcementService.create(announcement);
        return Result.success("新增成功", id);
    }

    @PutMapping("/announcement/{id}")
    public Result<Void> updateAnnouncement(@PathVariable Integer id, @RequestBody Announcement announcement) {
        announcementService.update(id, announcement);
        return Result.success("修改成功");
    }

    @DeleteMapping("/announcement/{id}")
    public Result<Void> deleteAnnouncement(@PathVariable Integer id) {
        announcementService.delete(id);
        return Result.success("删除成功");
    }

    @PutMapping("/announcement/{id}/publish")
    public Result<Void> publishAnnouncement(@PathVariable Integer id) {
        announcementService.publish(id);
        return Result.success("发布成功");
    }

    @PutMapping("/announcement/{id}/offline")
    public Result<Void> offlineAnnouncement(@PathVariable Integer id) {
        announcementService.offline(id);
        return Result.success("下架成功");
    }
}
