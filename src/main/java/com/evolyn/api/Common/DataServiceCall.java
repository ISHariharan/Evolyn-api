package com.evolyn.api.Common;

import java.util.function.Supplier;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * Calls the Data Service and passes its answer (status + JSON body) straight back to the browser.
 *
 * Why the catch? A MicroProfile REST Client THROWS a WebApplicationException whenever the
 * Data Service answers 4xx/5xx. Without this, a harmless 404 "Transaction not found"
 * would turn into a 500 here. We unwrap the exception and forward the real response.
 */
public final class DataServiceCall {

    private DataServiceCall() {}

    public static Response forward(Supplier<Response> call) {
        try {
            return copy(call.get());
        } catch (WebApplicationException e) {
            return copy(e.getResponse());
        } catch (Exception e) {
            System.out.println("Data Service call failed : " + e);
            return Response.status(Response.Status.BAD_GATEWAY)
                    .type(MediaType.APPLICATION_JSON)
                    .entity("{\"message\":\"Data service is unavailable\"}")
                    .build();
        }
    }

    private static Response copy(Response upstream) {
        Response.ResponseBuilder out = Response.status(upstream.getStatus());
        try {
            if (upstream.hasEntity()) {
                out.type(MediaType.APPLICATION_JSON).entity(upstream.readEntity(String.class));
            }
        } catch (Exception ignored) {
            // body already consumed or empty: forward the status alone
        }
        return out.build();
    }
}
