package org.limitless.backend.service;

import org.junit.jupiter.api.*;
import org.limitless.backend.entity.Activity;
import org.limitless.backend.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ActivityServiceTest {

    @Autowired
    private ActivityService activityService;

    @Test
    @Order(1)
    void selectById_shouldReturnActivity() {
        System.out.println("\n========== ActivityServiceTest.selectById ==========");
        // 通过分页查询获取一条活动
        org.limitless.backend.dto.ActivityPageRequest pageReq = new org.limitless.backend.dto.ActivityPageRequest();
        pageReq.setPageNumber(1);
        pageReq.setPageSize(10);
        var pageResult = activityService.selectPage(pageReq);

        if (!pageResult.getList().isEmpty()) {
            Activity activity = pageResult.getList().get(0);
            System.out.println("id: " + activity.getId());
            System.out.println("activityNo: " + activity.getActivityNo());
            System.out.println("title: " + activity.getTitle());
            System.out.println("categoryName: " + activity.getCategoryName());
            System.out.println("location: " + activity.getLocation());
            System.out.println("status: " + activity.getStatus());
            System.out.println("feeType: " + activity.getFeeType());
            Assertions.assertNotNull(activity.getTitle());
        }
    }

    @Test
    @Order(2)
    void create_shouldInsertActivity() {
        System.out.println("\n========== ActivityServiceTest.create ==========");
        org.limitless.backend.dto.ActivityCreateRequest request =
                new org.limitless.backend.dto.ActivityCreateRequest();
        request.setTitle("2026年冬季攀岩交流赛");
        request.setCategoryId(1);
        request.setLocation("极限运动馆1层攀岩区");
        request.setStartTime("2026-12-01 09:00:00");
        request.setEndTime("2026-12-03 18:00:00");
        request.setRules("1.年满14周岁可参赛");
        request.setDescription("冬季攀岩交流赛");
        request.setContact("0756-8888100");
        request.setFeeType("FREE");

        Integer id = activityService.create(request);
        System.out.println("created activityId: " + id);
        Assertions.assertNotNull(id);
    }

    @Test
    @Order(3)
    void update_shouldModifyTime() {
        System.out.println("\n========== ActivityServiceTest.update_修改时间 ==========");
        org.limitless.backend.dto.ActivityUpdateRequest updateReq =
                new org.limitless.backend.dto.ActivityUpdateRequest();
        updateReq.setStartTime("2026-12-10 10:00:00");
        updateReq.setEndTime("2026-12-12 16:00:00");
        updateReq.setTitle("更新后的标题");

        activityService.update(3, updateReq);
        System.out.println("活动 3 的时间已更新为: 2026-12-10 10:00 ~ 2026-12-12 16:00");
        System.out.println("标题更新为: 更新后的标题");
    }
}
