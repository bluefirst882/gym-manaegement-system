package org.limitless.backend.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.limitless.backend.common.PageRequest;

/**
 * 场地分类分页查询请求
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class VenueCategoryPageRequest extends PageRequest {
    private String categoryName;
}
