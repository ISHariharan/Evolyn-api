package com.evolyn.api.Auth;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

/**
 * Login tokens: proof that "this request comes from user X", which the browser
 * can't forge.
 *
 * A token is two parts joined by a dot:
 *   payload    base64url of {"sub":"<user id>","exp":<expiry, epoch seconds>}
 *   signature  HMAC-SHA256 of the payload with a secret only this server knows
 *
 * Anyone can read the payload, but changing it (e.g. to someone else's id)
 * breaks the signature, so the server rejects it. Same idea as a JWT, kept
 * small and dependency-free so every step is visible.
 *
 * The secret comes from the AUTH_TOKEN_SECRET environment variable. If it's
 * missing (e.g. on your laptop), a random one is made at start-up: that's safe,
 * it only means everyone has to log in again after a restart.
 */
@ApplicationScoped // one instance for the whole app, so login and the filter share the secret
public class TokenService {

    private static final Base64.Encoder B64 = Base64.getUrlEncoder().withoutPadding();
    private static final Base64.Decoder B64D = Base64.getUrlDecoder();

    private byte[] secret;
    private Duration lifetime;
    private Clock clock;

    // Needed by CDI to create its proxy; the real values come from the constructor below
    protected TokenService() {}

    @Inject
    public TokenService(@ConfigProperty(name = "evolyn.auth.token-secret", defaultValue = "") String secret,
                        @ConfigProperty(name = "evolyn.auth.token-days", defaultValue = "7") int days) {
        this(secretBytes(secret), Duration.ofDays(days), Clock.systemUTC());
    }

    // For tests: a fixed secret and clock
    TokenService(byte[] secret, Duration lifetime, Clock clock) {
        this.secret = secret;
        this.lifetime = lifetime;
        this.clock = clock;
    }

    public record Issued(String token, Instant expiresAt) {}

    public Issued issue(UUID userId) {
        Instant expiresAt = clock.instant().plus(lifetime);
        String payload = Json.createObjectBuilder()
                .add("sub", userId.toString())
                .add("exp", expiresAt.getEpochSecond())
                .build().toString();
        String body = B64.encodeToString(payload.getBytes(StandardCharsets.UTF_8));
        return new Issued(body + "." + B64.encodeToString(sign(body)), expiresAt);
    }

    /** The user id inside a valid, unexpired token; empty for anything else. */
    public Optional<UUID> verify(String token) {
        if (token == null) return Optional.empty();
        int dot = token.indexOf('.');
        if (dot <= 0 || dot != token.lastIndexOf('.')) return Optional.empty();
        String body = token.substring(0, dot);
        try {
            byte[] given = B64D.decode(token.substring(dot + 1));
            // Constant-time comparison, so the time taken doesn't leak how much matched
            if (!MessageDigest.isEqual(given, sign(body))) return Optional.empty();
            try (JsonReader reader = Json.createReader(new java.io.StringReader(
                    new String(B64D.decode(body), StandardCharsets.UTF_8)))) {
                JsonObject payload = reader.readObject();
                if (clock.instant().getEpochSecond() >= payload.getJsonNumber("exp").longValue()) {
                    return Optional.empty(); // expired
                }
                return Optional.of(UUID.fromString(payload.getString("sub")));
            }
        } catch (RuntimeException e) {
            return Optional.empty(); // not base64, not JSON, missing fields...
        }
    }

    private byte[] sign(String body) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret, "HmacSHA256"));
            return mac.doFinal(body.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new IllegalStateException("HmacSHA256 unavailable", e);
        }
    }

    private static byte[] secretBytes(String configured) {
        if (configured == null || configured.isBlank()) {
            System.out.println("WARNING: AUTH_TOKEN_SECRET is not set. Using a random secret: "
                    + "everyone will have to log in again after a restart. Set it on Render.");
            byte[] random = new byte[32];
            new SecureRandom().nextBytes(random);
            return random;
        }
        if (configured.length() < 32) {
            System.out.println("WARNING: AUTH_TOKEN_SECRET is shorter than 32 characters; use a longer one.");
        }
        return configured.getBytes(StandardCharsets.UTF_8);
    }
}
