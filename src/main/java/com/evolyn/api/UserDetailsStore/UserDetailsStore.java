package com.evolyn.api.UserDetailsStore;

import com.evolyn.api.Common.DataServiceCall;

import java.util.Map;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;
import jakarta.validation.Valid;
import jakarta.validation.executable.ValidateOnExecution;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;

@Path("/evolyn/api/store")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@ValidateOnExecution
public class UserDetailsStore {

    @Inject
    @RestClient
    EvolynDataServiceAuthStore authStore;

    
    @POST
    @Path("/userdetails")
    public Response userDetailsStore(@Valid UserDetailsStoreRequestValidation Request){
        // forward() passes the Data Service's errors (email taken, Profile ID taken...) straight through;
        // before, a 400 from it was reported as "Successfully Saved"
        Response response = DataServiceCall.forward(() -> authStore.storeUserDetails(Request));
        if (response.getStatus() >= 300) {
            return response;
        }
        return Response.ok(Map.of("message", "Successfully Saved"), MediaType.APPLICATION_JSON).build();
    }
}
