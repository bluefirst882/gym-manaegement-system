package org.limitless.backend.service;

import java.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/** Short-lived duplicate-submit and rate-limit guard. MySQL remains the source of truth. */
@Service
public class RedisRequestGuard {
    private static final Logger log = LoggerFactory.getLogger(RedisRequestGuard.class);
    private static final Duration IDEMPOTENCY_TTL = Duration.ofMinutes(10);
    private static final Duration RATE_WINDOW = Duration.ofSeconds(10);
    private static final int RATE_LIMIT = 5;

    private final StringRedisTemplate redis;

    public RedisRequestGuard(StringRedisTemplate redis) {
        this.redis = redis;
    }

    public RedisRequestGuard() {
        this.redis = null;
    }

    public Decision begin(String operation, int userId, String requestId) {
        if (requestId == null || requestId.isBlank() || redis == null) {
            return Decision.granted();
        }
        String key = idempotencyKey(operation, userId, requestId);
        try {
            String current = redis.opsForValue().get(key);
            if (current != null && current.startsWith("DONE:")) {
                return Decision.completed(current.substring("DONE:".length()));
            }
            Boolean created = redis.opsForValue().setIfAbsent(key, "PROCESSING", IDEMPOTENCY_TTL);
            if (Boolean.TRUE.equals(created)) return Decision.granted();
            throw new org.limitless.backend.common.BusinessException(409, "请求正在处理中，请勿重复提交");
        } catch (org.limitless.backend.common.BusinessException ex) {
            throw ex;
        } catch (Exception ex) {
            log.warn("Redis 幂等校验不可用，按数据库事务降级处理: {}", ex.toString());
            return Decision.granted();
        }
    }

    public void complete(String operation, int userId, String requestId, String resultId) {
        if (requestId == null || requestId.isBlank() || redis == null) return;
        try {
            redis.opsForValue().set(idempotencyKey(operation, userId, requestId),
                    "DONE:" + resultId, IDEMPOTENCY_TTL);
        } catch (Exception ex) {
            log.warn("Redis 幂等结果写入失败，数据库结果仍已提交: {}", ex.toString());
        }
    }

    public void release(String operation, int userId, String requestId) {
        if (requestId == null || requestId.isBlank() || redis == null) return;
        try {
            redis.delete(idempotencyKey(operation, userId, requestId));
        } catch (Exception ex) {
            log.warn("Redis 幂等键清理失败: {}", ex.toString());
        }
    }

    public void checkRate(String operation, int userId) {
        if (redis == null) return;
        try {
            String key = "gym:rate:" + operation + ":" + userId;
            Long count = redis.opsForValue().increment(key);
            if (count != null && count == 1L) redis.expire(key, RATE_WINDOW);
            if (count != null && count > RATE_LIMIT) {
                throw new org.limitless.backend.common.BusinessException(429, "操作过于频繁，请稍后重试");
            }
        } catch (org.limitless.backend.common.BusinessException ex) {
            throw ex;
        } catch (Exception ex) {
            log.warn("Redis 限流不可用，按放行策略降级: {}", ex.toString());
        }
    }

    private String idempotencyKey(String operation, int userId, String requestId) {
        return "gym:idempotency:" + operation + ":" + userId + ":" + requestId;
    }

    public record Decision(boolean acquired, String resultId) {
        static Decision granted() { return new Decision(true, null); }
        static Decision completed(String resultId) { return new Decision(false, resultId); }
    }
}
