package com.evolyn.api.Common;

import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import jakarta.annotation.Priority;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.eclipse.microprofile.config.ConfigProvider;

/**
 * Every call from this API to the Data Service goes through here.
 *
 * Both run on Render's free plan and talk over the public internet, through Cloudflare.
 * Cloudflare sometimes answers a burst of calls from Render's shared servers with
 * 429 "Too Many Requests" (or a "Just a moment..." challenge page) before the call ever
 * reaches the Data Service. So:
 *
 * 1. Pacing: at most N calls are in flight at once (evolyn.data-service.max-concurrent),
 *    so a page that loads 9 things doesn't fire 9 calls in the same instant.
 * 2. Retry: a 429 is retried after a short, growing, slightly random wait.
 *    Safe for POST/PUT too: a 429 here means the call never reached the Data Service.
 * 3. If it's still 429 after the last try, the browser gets a clean JSON message
 *    instead of Cloudflare's HTML page.
 */
@DataServiceRetry
@Interceptor
@Priority(Interceptor.Priority.PLATFORM_AFTER)
public class DataServiceRetryInterceptor {

    static final int TOO_MANY_REQUESTS = 429;
    static final String BUSY_JSON =
            "{\"message\":\"The server is busy right now. Please try again in a moment.\",\"code\":\"BUSY\"}";

    private static final Semaphore IN_FLIGHT = new Semaphore(
            ConfigProvider.getConfig().getOptionalValue("evolyn.data-service.max-concurrent", Integer.class).orElse(4),
            true);

    private static final RetryPolicy POLICY = new RetryPolicy(
            ConfigProvider.getConfig().getOptionalValue("evolyn.data-service.retries", Integer.class).orElse(3),
            ConfigProvider.getConfig().getOptionalValue("evolyn.data-service.retry-base-ms", Long.class).orElse(400L));

    @AroundInvoke
    public Object call(InvocationContext ctx) throws Exception {
        for (int attempt = 0; ; attempt++) {
            Object result;
            try {
                result = paced(ctx);
            } catch (WebApplicationException e) {
                if (!isBusy(e.getResponse())) throw e;
                if (!POLICY.canRetry(attempt)) throw new WebApplicationException(busy());
                close(e.getResponse());
                retryLater(ctx, attempt);
                continue;
            }
            if (result instanceof Response r && isBusy(r)) {
                if (!POLICY.canRetry(attempt)) {
                    close(r);
                    return busy();
                }
                close(r);
                retryLater(ctx, attempt);
                continue;
            }
            return result;
        }
    }

    private static Object paced(InvocationContext ctx) throws Exception {
        // Wait for a free slot, but never longer than a minute: better to send than to hang
        boolean slot = IN_FLIGHT.tryAcquire(60, TimeUnit.SECONDS);
        try {
            return ctx.proceed();
        } finally {
            if (slot) IN_FLIGHT.release();
        }
    }

    private static void retryLater(InvocationContext ctx, int attempt) throws InterruptedException {
        long wait = POLICY.delayMs(attempt, ThreadLocalRandom.current().nextDouble());
        System.out.println("Data Service answered 429 for " + ctx.getMethod().getDeclaringClass().getSimpleName()
                + "." + ctx.getMethod().getName() + "; retry " + (attempt + 1) + " in " + wait + " ms");
        Thread.sleep(wait);
    }

    private static boolean isBusy(Response r) {
        return r != null && r.getStatus() == TOO_MANY_REQUESTS;
    }

    private static void close(Response r) {
        try {
            if (r != null) r.close();
        } catch (Exception ignored) {
            // already closed
        }
    }

    static Response busy() {
        return Response.status(TOO_MANY_REQUESTS).type(MediaType.APPLICATION_JSON).entity(BUSY_JSON).build();
    }
}
