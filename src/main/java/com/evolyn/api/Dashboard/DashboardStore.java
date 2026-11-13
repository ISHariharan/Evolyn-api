package com.evolyn.api.Dashboard;

import java.util.UUID;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;

/** REST client for the Data Service's dashboard endpoints. */
@Path("/evolyn/api/internal/dashboard")
@RegisterRestClient(configKey = "evolyn-data-service")
public interface DashboardStore {

    @GET
    @Path("/summary")
    Response summary(@QueryParam("userId") UUID userId, @QueryParam("from") String from, @QueryParam("to") String to);

    @GET
    @Path("/accounts")
    Response accounts(@QueryParam("userId") UUID userId, @QueryParam("today") String today);

    @GET
    @Path("/trend")
    Response trend(@QueryParam("userId") UUID userId, @QueryParam("from") String from, @QueryParam("to") String to);
}
