package com.evolyn.api.UserDetailsVerify;

import com.evolyn.api.Common.DataServiceRetry;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.Consumes;
import java.util.UUID;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Path;

@Path("evolyn/api/internal")
@DataServiceRetry
@RegisterRestClient(configKey = "evolyn-data-service")
@Consumes(MediaType.APPLICATION_JSON)
public interface UserDetailsVerifyStore {
    
    @POST
    @Path("/verify/userdetails")
    Response verifyUserDetails(UserDetailsVerifyValidation Request);

    // One user's own details, by id
    @GET
    @Path("/users/{userId}")
    Response userById(@PathParam("userId") UUID userId);
}
