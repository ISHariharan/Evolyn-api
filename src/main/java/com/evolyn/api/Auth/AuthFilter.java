package com.evolyn.api.Auth;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonReader;
import jakarta.json.JsonStructure;
import jakarta.json.JsonValue;
import jakarta.ws.rs.HttpMethod;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.PreMatching;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.ext.Provider;

/**
 * Runs before every request to Evolyn-API.
 *
 * 1. Anything under /evolyn/api/ needs "Authorization: Bearer <login token>",
 *    except logging in, signing up and the public activity catalogue.
 *    No token, or a forged/expired one -> 401, and the browser logs out.
 *
 * 2. The token says who you are, so the browser no longer gets a say:
 *    any "userId" in the URL (?userId=...) or in a JSON body is REPLACED with
 *    the token's user before the request reaches an endpoint. Every endpoint
 *    already reads userId from one of those two places, so they're all
 *    protected at once, including future ones that follow the same pattern.
 *
 * @PreMatching: runs before JAX-RS picks the endpoint, which is what allows
 * rewriting the URL.
 */
@Provider
@PreMatching
@Priority(Priorities.AUTHENTICATION)
@ApplicationScoped
public class AuthFilter implements ContainerRequestFilter {

    static final String USER_ID = "userId";

    @Inject
    TokenService tokens;

    @Override
    public void filter(ContainerRequestContext request) throws IOException {
        String path = stripSlashes(request.getUriInfo().getPath());
        String method = request.getMethod();

        if (HttpMethod.OPTIONS.equals(method) || !path.startsWith("evolyn/api/") || isPublic(method, path)) {
            return;
        }

        Optional<UUID> user = tokenUser(request.getHeaderString(HttpHeaders.AUTHORIZATION));
        if (user.isEmpty()) {
            request.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                    .type(MediaType.APPLICATION_JSON)
                    .entity(Map.of("message", "Please log in again", "code", "UNAUTHENTICATED"))
                    .build());
            return;
        }
        String userId = user.get().toString();

        // ?userId=... always becomes the token's user (added if missing)
        URI uri = UriBuilder.fromUri(request.getUriInfo().getRequestUri())
                .replaceQueryParam(USER_ID, userId)
                .build();
        request.setRequestUri(request.getUriInfo().getBaseUri(), uri);

        // { ..., "userId": ... } in a JSON body likewise
        if (request.hasEntity() && isJson(request.getMediaType())) {
            replaceUserIdInBody(request, userId);
        }
    }

    // Logging in, signing up (and checking a Profile ID is free while doing so),
    // and the activity catalogue (public reference data)
    private static boolean isPublic(String method, String path) {
        return (HttpMethod.POST.equals(method) && path.equals("evolyn/api/verify/userdetails"))
                || (HttpMethod.POST.equals(method) && path.equals("evolyn/api/store/userdetails"))
                || (HttpMethod.GET.equals(method) && path.startsWith("evolyn/api/profile-ids/")
                        && path.endsWith("/availability"))
                || (HttpMethod.GET.equals(method) && (path.equals("evolyn/api/activity-definitions")
                        || path.startsWith("evolyn/api/activity-definitions/")));
    }

    private Optional<UUID> tokenUser(String authorization) {
        if (authorization == null || !authorization.regionMatches(true, 0, "Bearer ", 0, 7)) {
            return Optional.empty();
        }
        return tokens.verify(authorization.substring(7).trim());
    }

    private static void replaceUserIdInBody(ContainerRequestContext request, String userId) throws IOException {
        byte[] raw = request.getEntityStream().readAllBytes();
        byte[] out = raw;
        try (JsonReader reader = Json.createReader(new StringReader(new String(raw, StandardCharsets.UTF_8)))) {
            JsonStructure json = reader.read();
            if (json.getValueType() == JsonValue.ValueType.OBJECT) {
                out = Json.createObjectBuilder(json.asJsonObject()).add(USER_ID, userId).build()
                        .toString().getBytes(StandardCharsets.UTF_8);
            }
        } catch (RuntimeException notJson) {
            // Not valid JSON: leave it as it was; the endpoint will reject it
        }
        request.setEntityStream(new ByteArrayInputStream(out));
        request.getHeaders().remove(HttpHeaders.CONTENT_LENGTH); // the body's length changed
    }

    private static boolean isJson(MediaType type) {
        return type != null && type.isCompatible(MediaType.APPLICATION_JSON_TYPE);
    }

    private static String stripSlashes(String path) {
        String p = path == null ? "" : path;
        while (p.startsWith("/")) p = p.substring(1);
        return p;
    }
}
