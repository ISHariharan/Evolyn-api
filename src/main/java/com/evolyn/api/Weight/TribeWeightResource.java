package com.evolyn.api.Weight;

import java.util.UUID;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import com.evolyn.api.Common.DataServiceCall;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
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
 * Weight change in a tribe (login token needed; members only):
 *   GET /evolyn/api/tribes/{id}/weight-progress?month=2026-11   each member's change in kg, if they share it
 *   GET /evolyn/api/weight-sharing                              do you share your weight change?
 *   PUT /evolyn/api/weight-sharing  { "shareWeightChange": true }
 *
 * Other members never get anyone's weight, only the change (see the Data Service's TribeWeightService).
 * Class path is "/evolyn/api" so it can't swallow GET /tribes/{id} (see GoalResource).
 */
@Path("/evolyn/api")
@ValidateOnExecution
@Produces(MediaType.APPLICATION_JSON)
public class TribeWeightResource {

    private static final String MONTH = "^\\d{4}-(0[1-9]|1[0-2])$";

    @Inject
    @RestClient
    TribeWeightStore store;

    @GET @Path("/tribes/{tribeId}/weight-progress")
    public Response progress(@PathParam("tribeId") UUID tribeId, @QueryParam("userId") UUID userId,
                             @Pattern(regexp = MONTH, message = "month must look like 2026-11") @QueryParam("month") String month) {
        return DataServiceCall.forward(() -> store.progress(tribeId, userId, month));
    }

    @GET @Path("/weight-sharing")
    public Response sharing(@QueryParam("userId") UUID userId) {
        return DataServiceCall.forward(() -> store.sharing(userId));
    }

    @PUT @Path("/weight-sharing")
    public Response setSharing(@QueryParam("userId") UUID userId, @Valid WeightSharingDTO body) {
        return DataServiceCall.forward(() -> store.setSharing(userId, body));
    }
}
