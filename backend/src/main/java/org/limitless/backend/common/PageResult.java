package org.limitless.backend.common;

import lombok.Data;
import java.util.List;

/**
 * 分页响应结果
 */
@Data
public class PageResult<T> {
    private List<T> list;
    private int pageNumber;
    private int pageSize;
    private int totalPage;
    private long totalRow;

    public PageResult() {}

    public PageResult(List<T> list, int pageNumber, int pageSize, long totalRow) {
        this.list = list;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalRow = totalRow;
        this.totalPage = (int) Math.ceil((double) totalRow / pageSize);
    }
}
