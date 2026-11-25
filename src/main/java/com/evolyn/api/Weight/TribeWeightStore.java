package com.evolyn.api.Weight;

import java.util.UUID;

import com.evolyn.api.Common.DataServiceRetry;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/** REST client for the Data Service's tribe weight progress and weight sharing endpoints. */
@Path("/evolyn/api/internal")
@DataServiceRetry
@RegisterRestClient(configKey = "evolyn-data-service")
@Consumes(MediaType.APPLICATION_JSON)
public interface TribeWeightStore {

    @GET @Path("/tribes/{tribeId}/weight-progress")
    Response progress(@PathParam("tribeId") UUID tribeId, @QueryParam("userId") UUID userId, @QueryParam("month") String month);

    @GET @Path("/weight-sharing")
    Response sharing(@QueryParam("userId") UUID userId);

    @PUT @Path("/weight-sharing")
    Response setSharing(@QueryParam("userId") UUID userId, WeightSharingDTO body);
}
