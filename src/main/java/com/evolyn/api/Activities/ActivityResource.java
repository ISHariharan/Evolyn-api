package com.evolyn.api.Activities;

import java.util.UUID;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import com.evolyn.api.Common.DataServiceCall;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.executable.ValidateOnExecution;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * Public endpoints for your own activities:
 *   POST   /evolyn/api/activities                      log one (calories are worked out by the server)
 *   GET    /evolyn/api/activities?userId=&from=&to=    list with totals (default: this month so far)
 *   GET    /evolyn/api/activities/{id}?userId=
 *   PUT    /evolyn/api/activities/{id}
 *   DELETE /evolyn/api/activities/{id}?userId=
 *
 * 409 answers carry a "code": OVERLAP (confirm with allowOverlap=true) or
 * WEIGHT_REQUIRED (save a weight first).
 */
@Path("/evolyn/api/activities")
@ValidateOnExecution
@Produces(MediaType.APPLICATION_JSON)
public class ActivityResource {

    private static final String ISO_DATE = "^\\d{4}-\\d{2}-\\d{2}$";

    @Inject
    @RestClient
    ActivityStore activityStore;

    @POST
    public Response createActivity(@Valid ActivityDTO request) {
        return DataServiceCall.forward(() -> activityStore.createActivity(request));
    }

    @GET
    public Response listActivities(
            @NotNull(message = "userId is required") @QueryParam("userId") UUID userId,
            @Pattern(regexp = ISO_DATE, message = "from must be YYYY-MM-DD") @QueryParam("from") String from,
            @Pattern(regexp = ISO_DATE, message = "to must be YYYY-MM-DD") @QueryParam("to") String to) {
        return DataServiceCall.forward(() -> activityStore.listActivities(userId, from, to));
    }

    @GET
    @Path("/{activityId}")
    public Response getActivity(@PathParam("activityId") UUID activityId,
                                @NotNull(message = "userId is required") @QueryParam("userId") UUID userId) {
        return DataServiceCall.forward(() -> activityStore.getActivity(activityId, userId));
    }

    @PUT
    @Path("/{activityId}")
    public Response updateActivity(@PathParam("activityId") UUID activityId, @Valid ActivityDTO request) {
        return DataServiceCall.forward(() -> activityStore.updateActivity(activityId, request));
    }

    @DELETE
    @Path("/{activityId}")
    public Response deleteActivity(@PathParam("activityId") UUID activityId,
                                   @NotNull(message = "userId is required") @QueryParam("userId") UUID userId) {
        return DataServiceCall.forward(() -> activityStore.deleteActivity(activityId, userId));
    }
}
