package com.evolyn.api.BankDetails.StoreDetails;

import com.evolyn.api.UserDetailsStore.UserDetailsStoreRequestValidation;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;


@Path("/evolyn/api/internal/expenses")
@RegisterRestClient(configKey="evolyn-data-service")
@Consumes(MediaType.APPLICATION_JSON)
public interface EDSBankDetails {

    @POST
    @Path("/account/add")
    Response EDSBankDetails(BankDetailsDTO Request);
}
