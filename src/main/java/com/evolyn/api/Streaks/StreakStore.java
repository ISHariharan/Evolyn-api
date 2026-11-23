package com.evolyn.api.Streaks;

import java.util.UUID;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/** REST client for the Data Service's streak endpoints. */
@Path("/evolyn/api/internal")
@RegisterRestClient(configKey = "evolyn-data-service")
@Consumes(MediaType.APPLICATION_JSON)
public interface StreakStore {

    @GET @Path("/tribes/{tribeId}/streaks")
    Response tribe(@PathParam("tribeId") UUID tribeId, @QueryParam("userId") UUID userId);

    @PUT @Path("/tribes/{tribeId}/streak-rules")
    Response rules(@PathParam("tribeId") UUID tribeId, @QueryParam("userId") UUID userId, StreakRulesDTO body);

    @GET @Path("/streaks/me")
    Response mine(@QueryParam("userId") UUID userId);
}
