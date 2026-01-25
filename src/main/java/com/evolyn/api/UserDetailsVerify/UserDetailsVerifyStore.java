package com.evolyn.api.UserDetailsVerify;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

@Path("evolyn/api/internal")
@RegisterRestClient(configKey = "evolyn-data-service")
@Consumes(MediaType.APPLICATION_JSON)
public interface UserDetailsVerifyStore {
    
    @POST
    @Path("/verify/userdetails")
    Response verifyUserDetails(UserDetailsVerifyValidation Request);
}
