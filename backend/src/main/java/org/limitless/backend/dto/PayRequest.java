package org.limitless.backend.dto;

import lombok.Data;

/**
 * 支付请求
 */
@Data
public class PayRequest {
    private String paymentMethod;
}
