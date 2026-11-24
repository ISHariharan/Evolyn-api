package com.evolyn.api.Activities;

import java.util.UUID;

import com.evolyn.api.Common.DataServiceRetry;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/** REST client for the Data Service's activity endpoints. */
@Path("/evolyn/api/internal/activities")
@DataServiceRetry
@RegisterRestClient(configKey = "evolyn-data-service")
@Consumes(MediaType.APPLICATION_JSON)
public interface ActivityStore {

    @POST
    Response createActivity(ActivityDTO request);

    @GET
    Response listActivities(@QueryParam("userId") UUID userId, @QueryParam("from") String from, @QueryParam("to") String to);

    @GET
    @Path("/{activityId}")
    Response getActivity(@PathParam("activityId") UUID activityId, @QueryParam("userId") UUID userId);

    @PUT
    @Path("/{activityId}")
    Response updateActivity(@PathParam("activityId") UUID activityId, ActivityDTO request);

    @DELETE
    @Path("/{activityId}")
    Response deleteActivity(@PathParam("activityId") UUID activityId, @QueryParam("userId") UUID userId);
}
