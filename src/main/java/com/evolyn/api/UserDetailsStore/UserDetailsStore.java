package com.evolyn.api.UserDetailsStore;

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
        System.out.println("Received the data : " + Request.getPassword());
        authStore.storeUserDetails(Request);
        return Response.ok(Map.of("message", "Successfully Saved"), MediaType.APPLICATION_JSON).build();
    }
}
