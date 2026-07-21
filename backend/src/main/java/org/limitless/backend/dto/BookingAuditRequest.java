package org.limitless.backend.dto;

import lombok.Data;

/** 管理员预约审核请求 */
@Data
public class BookingAuditRequest {
    /** APPROVED 通过；REJECTED 拒绝 */
    private String status;
}
