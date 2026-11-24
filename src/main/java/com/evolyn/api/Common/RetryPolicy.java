package com.evolyn.api.Common;

/**
 * How many times to retry and how long to wait: base, 3x base, 9x base ... plus up to 50% random,
 * so calls that failed together don't all come back at the same moment.
 * With the defaults (3 retries, 400 ms): about 0.4 s, 1.2 s, 3.6 s; roughly 5 to 8 s in all.
 */
public record RetryPolicy(int retries, long baseMs) {

    public boolean canRetry(int attempt) {
        return attempt < retries;
    }

    /** attempt: 0 for the first retry. jitter: 0.0 to 1.0. */
    public long delayMs(int attempt, double jitter) {
        long backoff = baseMs * (long) Math.pow(3, attempt);
        return backoff + Math.round(backoff * 0.5 * Math.max(0, Math.min(1, jitter)));
    }
}
