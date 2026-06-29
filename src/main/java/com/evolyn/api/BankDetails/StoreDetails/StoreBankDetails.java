package com.evolyn.api.BankDetails.StoreDetails;

import java.util.Map;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.OPTIONS;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Produces;
import jakarta.validation.Valid;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;


@Path("/evolyn/api/expenses")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class StoreBankDetails {

    @Inject
    @RestClient
    public EDSBankDetails edsBankDetails;

    @POST
    @Path("/account/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response storeBankDetails(@Valid BankDetailsDTO request) {
        Response response = edsBankDetails.EDSBankDetails(request);
        String responseBody = response.hasEntity() ? response.readEntity(String.class) : "";

        if (response.getStatus() >= 400) {
            return Response
                    .status(response.getStatus())
                    .entity(responseBody)
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }

        return Response
                .status(response.getStatus())
                .entity(Map.of("message", "Bank Details Saved"))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    @OPTIONS
    @Path("/account/add")
    public Response storeBankDetailsOptions() {
        return Response
                .ok()
                .header("Allow", "POST, OPTIONS")
                .build();
    }
}
