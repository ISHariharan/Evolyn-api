package com.evolyn.api.Common;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class RetryPolicyTest {

    private final RetryPolicy policy = new RetryPolicy(3, 400);

    @Test
    void retriesThreeTimesThenStops() {
        assertTrue(policy.canRetry(0));
        assertTrue(policy.canRetry(2));
        assertFalse(policy.canRetry(3));
    }

    @Test
    void waitsGrowThreefoldWithUpToHalfExtra() {
        assertEquals(400, policy.delayMs(0, 0));
        assertEquals(1200, policy.delayMs(1, 0));
        assertEquals(3600, policy.delayMs(2, 0));
        assertEquals(5400, policy.delayMs(2, 1));
        assertEquals(600, policy.delayMs(0, 7)); // jitter is capped at 1
    }

    @Test
    void noRetriesMeansOneTryOnly() {
        assertFalse(new RetryPolicy(0, 400).canRetry(0));
    }
}
