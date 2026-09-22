package org.limitless.backend.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

class RedisRequestGuardTest {
    @Test
    void noRedisConfiguredFallsBackToDatabaseGuard() {
        RedisRequestGuard guard = new RedisRequestGuard();

        RedisRequestGuard.Decision decision = guard.begin("booking-create", 7, "request-1");

        assertTrue(decision.acquired());
        assertDoesNotThrow(() -> guard.checkRate("booking-create", 7));
        assertDoesNotThrow(() -> guard.complete("booking-create", 7, "request-1", "12"));
    }

    @Test
    void completedRequestCanBeReplayed() {
        var redis = mock(org.springframework.data.redis.core.StringRedisTemplate.class);
        var values = mock(org.springframework.data.redis.core.ValueOperations.class);
        when(redis.opsForValue()).thenReturn(values);
        when(values.get("gym:idempotency:booking-create:7:request-1")).thenReturn("DONE:12");
        RedisRequestGuard guard = new RedisRequestGuard(redis);

        RedisRequestGuard.Decision decision = guard.begin("booking-create", 7, "request-1");

        assertEquals("12", decision.resultId());
        assertFalse(decision.acquired());
    }
}
