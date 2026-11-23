package com.evolyn.api.Auth;

import java.util.UUID;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import com.evolyn.api.Common.DataServiceCall;
import com.evolyn.api.UserDetailsVerify.UserDetailsVerifyStore;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * GET /evolyn/api/me -> the logged-in user's own details { id, email, firstName, lastName }.
 * userId isn't sent by the browser: AuthFilter fills it in from the login token.
 */
@Path("/evolyn/api/me")
@Produces(MediaType.APPLICATION_JSON)
public class MeResource {

    @Inject
    @RestClient
    UserDetailsVerifyStore users;

    @GET
    public Response me(@QueryParam("userId") UUID userId) {
        return DataServiceCall.forward(() -> users.userById(userId));
    }
}
