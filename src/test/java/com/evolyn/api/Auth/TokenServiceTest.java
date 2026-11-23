package com.evolyn.api.Auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;

class TokenServiceTest {

    private static final byte[] SECRET = "test-secret-test-secret-test-secret!".getBytes(StandardCharsets.UTF_8);
    private static final UUID USER = UUID.fromString("44444444-4444-4444-4444-444444444444");
    private static final Instant NOW = Instant.parse("2026-11-23T10:00:00Z");

    private static TokenService at(Instant now) {
        return new TokenService(SECRET, Duration.ofDays(7), Clock.fixed(now, ZoneOffset.UTC));
    }

    @Test
    void aFreshTokenGivesBackTheUser() {
        TokenService.Issued issued = at(NOW).issue(USER);
        assertEquals(Optional.of(USER), at(NOW).verify(issued.token()));
        assertEquals(NOW.plus(Duration.ofDays(7)), issued.expiresAt());
    }

    @Test
    void expiredTokensAreRejected() {
        String token = at(NOW).issue(USER).token();
        assertTrue(at(NOW.plus(Duration.ofDays(7))).verify(token).isEmpty());
        assertEquals(Optional.of(USER), at(NOW.plus(Duration.ofDays(6))).verify(token));
    }

    @Test
    void changingTheUserInsideBreaksTheSignature() {
        String token = at(NOW).issue(USER).token();
        String forgedPayload = Base64.getUrlEncoder().withoutPadding().encodeToString(
                ("{\"sub\":\"55555555-5555-5555-5555-555555555555\",\"exp\":" + NOW.plus(Duration.ofDays(7)).getEpochSecond() + "}")
                        .getBytes(StandardCharsets.UTF_8));
        String forged = forgedPayload + token.substring(token.indexOf('.'));
        assertTrue(at(NOW).verify(forged).isEmpty());
    }

    @Test
    void anotherServersSecretIsRejected() {
        String token = new TokenService("some-other-secret-some-other-secret".getBytes(StandardCharsets.UTF_8),
                Duration.ofDays(7), Clock.fixed(NOW, ZoneOffset.UTC)).issue(USER).token();
        assertTrue(at(NOW).verify(token).isEmpty());
    }

    @Test
    void garbageIsRejected() {
        TokenService s = at(NOW);
        assertTrue(s.verify(null).isEmpty());
        assertTrue(s.verify("").isEmpty());
        assertTrue(s.verify("abc").isEmpty());
        assertTrue(s.verify("a.b.c").isEmpty());
        assertTrue(s.verify("!!!.???").isEmpty());
    }
}
