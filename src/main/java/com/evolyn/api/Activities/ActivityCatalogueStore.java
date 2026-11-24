package com.evolyn.api.Activities;

import com.evolyn.api.Common.DataServiceRetry;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;

/** REST client for the Data Service's read-only activity catalogue. */
@Path("/evolyn/api/internal/activity-definitions")
@DataServiceRetry
@RegisterRestClient(configKey = "evolyn-data-service")
public interface ActivityCatalogueStore {

    @GET
    Response catalogue();

    @GET
    @Path("/{code}")
    Response activity(@PathParam("code") String code);
}
