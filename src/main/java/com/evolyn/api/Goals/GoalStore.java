package com.evolyn.api.Goals;

import java.util.UUID;

import com.evolyn.api.Common.DataServiceRetry;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/** REST client for the Data Service's tribe dashboard endpoint. */
@Path("/evolyn/api/internal/tribes/{tribeId}")
@DataServiceRetry
@RegisterRestClient(configKey = "evolyn-data-service")
@Consumes(MediaType.APPLICATION_JSON)
public interface GoalStore {

    @GET @Path("/dashboard")
    Response dashboard(@PathParam("tribeId") UUID tribeId, @QueryParam("userId") UUID userId,
                       @QueryParam("month") String month);
}
