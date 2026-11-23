package com.evolyn.api.Activities;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import com.evolyn.api.Common.DataServiceCall;

import jakarta.inject.Inject;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.executable.ValidateOnExecution;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * The activity catalogue (2024 Adult Compendium, ~1,100 activities):
 *   GET /evolyn/api/activity-definitions          everything, for the activity picker
 *   GET /evolyn/api/activity-definitions/{code}   one activity, e.g. 15330
 *
 * The list only changes when a new Compendium edition is imported, so browsers
 * may keep it for a day instead of downloading ~120 KB on every visit.
 */
@Path("/evolyn/api/activity-definitions")
@ValidateOnExecution
@Produces(MediaType.APPLICATION_JSON)
public class ActivityCatalogueResource {

    static final String CACHE_ONE_DAY = "public, max-age=86400";

    @Inject
    @RestClient
    ActivityCatalogueStore catalogueStore;

    @GET
    public Response catalogue() {
        return cacheable(DataServiceCall.forward(() -> catalogueStore.catalogue()));
    }

    @GET
    @Path("/{code}")
    public Response activity(@Pattern(regexp = "\\d{5}", message = "Activity code must be 5 digits")
                             @PathParam("code") String code) {
        return cacheable(DataServiceCall.forward(() -> catalogueStore.activity(code)));
    }

    // Only successful answers are cached; an error must not stick around for a day
    private static Response cacheable(Response response) {
        if (response.getStatus() != 200) return response;
        return Response.fromResponse(response).header("Cache-Control", CACHE_ONE_DAY).build();
    }
}
