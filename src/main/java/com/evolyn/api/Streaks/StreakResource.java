package com.evolyn.api.Streaks;

import java.util.UUID;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import com.evolyn.api.Common.DataServiceCall;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.executable.ValidateOnExecution;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * Streaks (login token needed; userId comes from it):
 *   GET /evolyn/api/tribes/{id}/streaks          tribe streak, members' streaks, rules (members only)
 *   PUT /evolyn/api/tribes/{id}/streak-rules     { "minMinutes": 20, "memberPercent": 50 } (any member)
 *   GET /evolyn/api/streaks/me                   your own streak (20+ min of a type that counts)
 *
 * Class path is "/evolyn/api" like the other tribe resources, so none of them
 * takes over another's paths.
 */
@Path("/evolyn/api")
@ValidateOnExecution
@Produces(MediaType.APPLICATION_JSON)
public class StreakResource {

    @Inject
    @RestClient
    StreakStore store;

    @GET @Path("/tribes/{tribeId}/streaks")
    public Response tribe(@PathParam("tribeId") UUID tribeId, @QueryParam("userId") UUID userId) {
        return DataServiceCall.forward(() -> store.tribe(tribeId, userId));
    }

    @PUT @Path("/tribes/{tribeId}/streak-rules")
    public Response rules(@PathParam("tribeId") UUID tribeId, @QueryParam("userId") UUID userId, @Valid StreakRulesDTO body) {
        return DataServiceCall.forward(() -> store.rules(tribeId, userId, body));
    }

    @GET @Path("/streaks/me")
    public Response mine(@QueryParam("userId") UUID userId) {
        return DataServiceCall.forward(() -> store.mine(userId));
    }
}
