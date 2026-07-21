package org.limitless.backend.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 公告表 announcement
 */
@Data
public class Announcement {
    private Integer id;
    private String announcementNo;
    private String title;
    private String summary;
    private String coverImage;
    private String content;
    private Integer isTop;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
