package org.limitless.backend.dto;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 缴费状态修改请求
 */
@Data
public class PaymentStatusRequest {
    private String paymentStatus;
    private String paymentMethod;
    private BigDecimal amount;
}
