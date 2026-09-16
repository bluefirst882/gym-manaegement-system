package org.limitless.backend.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 活动分类表 activity_category
 */
@Data
public class ActivityCategory {
    private Integer id;
    private String categoryCode;
    private String categoryName;
    private String categoryDesc;
    private Integer sortOrder;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
