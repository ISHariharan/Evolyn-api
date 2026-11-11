package com.evolyn.api.Common;

import java.io.IOException;

import org.eclipse.microprofile.config.ConfigProvider;

import jakarta.ws.rs.client.ClientRequestContext;
import jakarta.ws.rs.client.ClientRequestFilter;

/**
 * Adds the shared secret to every request Evolyn-API sends to Evolyn-Data-Service.
 * The Data Service rejects calls without it, so only this API can reach its data.
 *
 * Registered for all REST clients through
 * evolyn-data-service/mp-rest/providers in microprofile-config.properties.
 */
public class InternalKeyHeader implements ClientRequestFilter {

    private static final String HEADER = "X-Internal-Key";

    private final String key = ConfigProvider.getConfig()
            .getOptionalValue("evolyn.internal-api-key", String.class)
            .orElse("");

    @Override
    public void filter(ClientRequestContext request) throws IOException {
        if (!key.isBlank()) {
            request.getHeaders().putSingle(HEADER, key);
        }
    }
}
