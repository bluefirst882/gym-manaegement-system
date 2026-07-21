package org.limitless.backend.dto;

import lombok.Data;

/**
 * 到店登记请求
 */
@Data
public class CheckinRequest {
    private String checkinTime;
    private Integer attendeeCount;
}
