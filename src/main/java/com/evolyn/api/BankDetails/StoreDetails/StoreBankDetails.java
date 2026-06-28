package com.evolyn.api.BankDetails.StoreDetails;

import java.util.Map;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Path;
import jakarta.validation.Valid;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


@Path("/evolyn/api/expenses")
@Consumes(MediaType.APPLICATION_JSON)
public class StoreBankDetails {

    public EDSBankDetails edsBankDetails;

    @Path("/account/add")
    public Response storeBankDetails(@Valid BankDetailsDTO request) {
        Response response = edsBankDetails.EDSBankDetails(request);
        if (response.getStatus() > 400) {
            return response
                    .status(response.getStatus())
                    .entity(response.readEntity(String.class))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }

        return response.ok(Map.of("message", "Bank Details Saved"), MediaType.APPLICATION_JSON).build();
    }
}
