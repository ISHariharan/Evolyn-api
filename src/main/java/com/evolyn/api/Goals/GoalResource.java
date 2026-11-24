package com.evolyn.api.Goals;

import java.util.UUID;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import com.evolyn.api.Common.DataServiceCall;

import jakarta.inject.Inject;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.executable.ValidateOnExecution;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * A tribe's monthly dashboard (login token needed; members only):
 *   GET /evolyn/api/tribes/{id}/dashboard?month=2026-11      goal, progress, daily totals, leaderboard, you
 *
 * The goal is the sum of the members' daily goals (see DailyGoalResource); nobody types it in.
 *
 * The class path is "/evolyn/api" (not ".../tribes/{tribeId}"): JAX-RS picks the
 * class with the longest matching path first, so a longer one here would
 * swallow GET /evolyn/api/tribes/{id} from TribeResource and answer 404.
 */
@Path("/evolyn/api")
@ValidateOnExecution
@Produces(MediaType.APPLICATION_JSON)
public class GoalResource {

    private static final String MONTH = "^\\d{4}-(0[1-9]|1[0-2])$";

    @Inject
    @RestClient
    GoalStore store;

    @GET @Path("/tribes/{tribeId}/dashboard")
    public Response dashboard(@PathParam("tribeId") UUID tribeId, @QueryParam("userId") UUID userId,
                              @Pattern(regexp = MONTH, message = "month must look like 2026-11") @QueryParam("month") String month) {
        return DataServiceCall.forward(() -> store.dashboard(tribeId, userId, month));
    }
}
