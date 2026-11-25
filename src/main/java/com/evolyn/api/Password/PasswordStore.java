package com.evolyn.api.Password;

import java.util.UUID;

import com.evolyn.api.Common.DataServiceRetry;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/** REST client for the Data Service's password and recovery-code endpoints. */
@Path("/evolyn/api/internal")
@DataServiceRetry
@RegisterRestClient(configKey = "evolyn-data-service")
@Consumes(MediaType.APPLICATION_JSON)
public interface PasswordStore {

    @GET @Path("/recovery-code")
    Response codeStatus(@QueryParam("userId") UUID userId);

    @POST @Path("/recovery-code")
    Response createCode(@QueryParam("userId") UUID userId, PasswordDTOs.NewCode body);

    @POST @Path("/password-reset")
    Response reset(PasswordDTOs.Reset body);

    @POST @Path("/password/change")
    Response change(@QueryParam("userId") UUID userId, PasswordDTOs.Change body);
}
