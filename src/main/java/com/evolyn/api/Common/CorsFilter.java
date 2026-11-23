package com.evolyn.api.Common;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.HttpMethod;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.container.PreMatching;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

import org.eclipse.microprofile.config.inject.ConfigProperty;

/**
 * Lets the Evolyn website call this API straight from the browser (CORS).
 *
 * The site used to reach the API through Vercel's /evolyn/api rewrite, so every call came
 * from Vercel's shared servers; Render's Cloudflare answered bursts of those with 429.
 * Now the browser calls Render itself, which needs these headers.
 *
 * Only origins in evolyn.cors.allowed-origins get them. Logins are a Bearer token in the
 * Authorization header (no cookies), so no credentials are allowed cross-site.
 *
 * Runs before AuthFilter so a preflight (OPTIONS) is answered without a token.
 */
@Provider
@PreMatching
@Priority(Priorities.AUTHENTICATION - 100)
@ApplicationScoped
public class CorsFilter implements ContainerRequestFilter, ContainerResponseFilter {

    static final String ORIGIN = "Origin";
    static final String ALLOW_METHODS = "GET, POST, PUT, DELETE, OPTIONS";
    static final String ALLOW_HEADERS = "Authorization, Content-Type, Accept";
    // Browsers cache the preflight this long (Chrome caps it at 2 hours), so most calls need no extra OPTIONS
    static final String MAX_AGE = "7200";

    private final Set<String> allowed;

    @Inject
    public CorsFilter(@ConfigProperty(name = "evolyn.cors.allowed-origins") String origins) {
        this.allowed = parse(origins);
    }

    // CDI needs a no-arg constructor for the proxy
    protected CorsFilter() {
        this.allowed = Set.of();
    }

    static Set<String> parse(String origins) {
        return Arrays.stream(origins == null ? new String[0] : origins.split(","))
                .map(String::trim)
                .map(o -> o.endsWith("/") ? o.substring(0, o.length() - 1) : o)
                .filter(o -> !o.isEmpty())
                .collect(Collectors.toUnmodifiableSet());
    }

    boolean isAllowed(String origin) {
        return origin != null && allowed.contains(origin);
    }

    @Override
    public void filter(ContainerRequestContext request) throws IOException {
        String origin = request.getHeaderString(ORIGIN);
        boolean preflight = HttpMethod.OPTIONS.equals(request.getMethod())
                && request.getHeaderString("Access-Control-Request-Method") != null;
        if (!preflight) {
            return;
        }
        Response.ResponseBuilder answer = Response.noContent().header("Vary", ORIGIN);
        if (isAllowed(origin)) {
            answer.header("Access-Control-Allow-Origin", origin)
                    .header("Access-Control-Allow-Methods", ALLOW_METHODS)
                    .header("Access-Control-Allow-Headers", ALLOW_HEADERS)
                    .header("Access-Control-Max-Age", MAX_AGE);
        }
        request.abortWith(answer.build());
    }

    @Override
    public void filter(ContainerRequestContext request, ContainerResponseContext response) {
        String origin = request.getHeaderString(ORIGIN);
        MultivaluedMap<String, Object> headers = response.getHeaders();
        if (!isAllowed(origin) || headers.containsKey("Access-Control-Allow-Origin")) {
            return;
        }
        headers.putSingle("Access-Control-Allow-Origin", origin);
        headers.add("Vary", ORIGIN);
    }
}
