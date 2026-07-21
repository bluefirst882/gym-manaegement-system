package org.limitless.backend.common;

import lombok.Data;

/**
 * 分页请求参数
 */
@Data
public class PageRequest {
    private int pageNumber = 1;
    private int pageSize = 10;
}
