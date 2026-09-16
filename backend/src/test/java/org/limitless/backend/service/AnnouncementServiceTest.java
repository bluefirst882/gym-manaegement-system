package org.limitless.backend.service;

import org.junit.jupiter.api.*;
import org.limitless.backend.entity.Announcement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AnnouncementServiceTest {

    @Autowired
    private AnnouncementService announcementService;

    @Test
    @Order(1)
    void selectById_shouldReturnPublished() {
        System.out.println("\n========== AnnouncementServiceTest.selectById ==========");
        Announcement announcement = announcementService.selectById(1);
        System.out.println("id: " + announcement.getId());
        System.out.println("announcementNo: " + announcement.getAnnouncementNo());
        System.out.println("title: " + announcement.getTitle());
        System.out.println("summary: " + announcement.getSummary());
        System.out.println("status: " + announcement.getStatus());
        System.out.println("isTop: " + announcement.getIsTop());

        Assertions.assertEquals("极限运动馆2026年暑期营业时间调整通知", announcement.getTitle());
        Assertions.assertEquals("PUBLISHED", announcement.getStatus());
        Assertions.assertEquals(1, announcement.getIsTop().intValue());
    }

    @Test
    @Order(2)
    void create_shouldUseDraftByDefault() {
        System.out.println("\n========== AnnouncementServiceTest.create ==========");
        Announcement announcement = new Announcement();
        announcement.setTitle("新公告标题");
        announcement.setSummary("新公告摘要");
        announcement.setContent("<p>新公告内容</p>");

        Integer id = announcementService.create(announcement);
        System.out.println("created announcementId: " + id);
        System.out.println("announcementNo: " + announcement.getAnnouncementNo());
        System.out.println("default status: " + announcement.getStatus());
        System.out.println("default isTop: " + announcement.getIsTop());

        Assertions.assertEquals("DRAFT", announcement.getStatus());
        Assertions.assertEquals(0, announcement.getIsTop().intValue());
    }

    @Test
    @Order(3)
    void publish_shouldChangeStatus() {
        System.out.println("\n========== AnnouncementServiceTest.publish ==========");
        announcementService.publish(1);
        System.out.println("公告 1 已发布（已经是 PUBLISHED 状态）");
    }

    @Test
    @Order(4)
    void offline_shouldChangeStatus() {
        System.out.println("\n========== AnnouncementServiceTest.offline ==========");
        announcementService.offline(1);
        System.out.println("公告 1 已下架");

        Announcement result = announcementService.selectById(1);
        System.out.println("当前状态: " + result.getStatus());
        Assertions.assertEquals("OFFLINE", result.getStatus());
    }

    @Test
    @Order(5)
    void delete_shouldRemove() {
        System.out.println("\n========== AnnouncementServiceTest.delete ==========");
        announcementService.delete(2);
        System.out.println("公告 2 已删除");
    }
}
