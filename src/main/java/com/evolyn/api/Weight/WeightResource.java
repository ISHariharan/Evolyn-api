package com.evolyn.api.Weight;

import java.util.UUID;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import com.evolyn.api.Common.DataServiceCall;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.executable.ValidateOnExecution;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * Public endpoints for your own weight:
 *   PUT /evolyn/api/weights                                  save a day's weight (same day again = correction)
 *   GET /evolyn/api/weights?userId=&from=&to=                history, newest first
 *   GET /evolyn/api/weights/latest?userId=&onOrBefore=       the weight that applies on a day (404: none yet)
 */
@Path("/evolyn/api/weights")
@ValidateOnExecution
@Produces(MediaType.APPLICATION_JSON)
public class WeightResource {

    private static final String ISO_DATE = "^\\d{4}-\\d{2}-\\d{2}$";

    @Inject
    @RestClient
    WeightStore weightStore;

    @PUT
    public Response saveWeight(@Valid WeightDTO request) {
        return DataServiceCall.forward(() -> weightStore.saveWeight(request));
    }

    @GET
    public Response history(
            @NotNull(message = "userId is required") @QueryParam("userId") UUID userId,
            @Pattern(regexp = ISO_DATE, message = "from must be YYYY-MM-DD") @QueryParam("from") String from,
            @Pattern(regexp = ISO_DATE, message = "to must be YYYY-MM-DD") @QueryParam("to") String to) {
        return DataServiceCall.forward(() -> weightStore.history(userId, from, to));
    }

    @GET
    @Path("/latest")
    public Response latest(
            @NotNull(message = "userId is required") @QueryParam("userId") UUID userId,
            @Pattern(regexp = ISO_DATE, message = "onOrBefore must be YYYY-MM-DD") @QueryParam("onOrBefore") String onOrBefore) {
        return DataServiceCall.forward(() -> weightStore.latest(userId, onOrBefore));
    }
}
