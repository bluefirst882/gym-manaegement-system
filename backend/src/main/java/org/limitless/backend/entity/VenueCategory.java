package org.limitless.backend.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 场地分类表 venue_category
 */
@Data
public class VenueCategory {
    private Integer id;
    private String categoryCode;
    private String categoryName;
    private String categoryDesc;
    private Integer sortOrder;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
