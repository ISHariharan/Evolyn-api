package com.evolyn.api.Weight;

import java.util.UUID;

import com.evolyn.api.Common.DataServiceRetry;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/** REST client for the Data Service's weight endpoints. */
@Path("/evolyn/api/internal/weights")
@DataServiceRetry
@RegisterRestClient(configKey = "evolyn-data-service")
@Consumes(MediaType.APPLICATION_JSON)
public interface WeightStore {

    @PUT
    Response saveWeight(WeightDTO request);

    @GET
    Response history(@QueryParam("userId") UUID userId, @QueryParam("from") String from, @QueryParam("to") String to);

    @GET
    @Path("/latest")
    Response latest(@QueryParam("userId") UUID userId, @QueryParam("onOrBefore") String onOrBefore);
}
