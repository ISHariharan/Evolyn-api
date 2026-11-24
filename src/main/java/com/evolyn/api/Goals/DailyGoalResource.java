package com.evolyn.api.Goals;

import java.util.UUID;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import com.evolyn.api.Common.DataServiceCall;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.executable.ValidateOnExecution;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * Your daily calorie goal (login token needed; userId comes from the token):
 *   GET /evolyn/api/daily-goal     your goal, today's and this month's progress
 *   PUT /evolyn/api/daily-goal     { "kcal": 400 }  applies from today; earlier days keep theirs
 *
 * Every tribe you're in adds your daily goals to its monthly target.
 */
@Path("/evolyn/api")
@ValidateOnExecution
@Produces(MediaType.APPLICATION_JSON)
public class DailyGoalResource {

    @Inject
    @RestClient
    DailyGoalStore store;

    @GET @Path("/daily-goal")
    public Response mine(@QueryParam("userId") UUID userId) {
        return DataServiceCall.forward(() -> store.mine(userId));
    }

    @PUT @Path("/daily-goal")
    public Response set(@QueryParam("userId") UUID userId, @Valid DailyGoalDTO body) {
        return DataServiceCall.forward(() -> store.set(userId, body));
    }
}
