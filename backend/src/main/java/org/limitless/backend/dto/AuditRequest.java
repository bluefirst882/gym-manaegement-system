package org.limitless.backend.dto;

import lombok.Data;

/**
 * 报名审核请求
 */
@Data
public class AuditRequest {
    private String auditStatus;
    private String auditComment;
}
