package com.evolyn.api.Goals;

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

/** REST client for the Data Service's daily goal endpoints. */
@Path("/evolyn/api/internal/daily-goal")
@DataServiceRetry
@RegisterRestClient(configKey = "evolyn-data-service")
@Consumes(MediaType.APPLICATION_JSON)
public interface DailyGoalStore {

    @GET
    Response mine(@QueryParam("userId") UUID userId);

    @PUT
    Response set(@QueryParam("userId") UUID userId, DailyGoalDTO body);
}
