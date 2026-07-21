package org.limitless.backend.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.limitless.backend.common.PageRequest;

/**
 * 活动分页查询请求
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ActivityPageRequest extends PageRequest {
    private String title;
    private Integer categoryId;
    private String status;
}
