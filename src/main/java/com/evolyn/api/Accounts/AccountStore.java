package com.evolyn.api.Accounts;

import java.util.UUID;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/** REST client for the Data Service's account endpoints. */
@Path("/evolyn/api/internal/accounts")
@RegisterRestClient(configKey = "evolyn-data-service")
@Consumes(MediaType.APPLICATION_JSON)
public interface AccountStore {

    @GET
    Response listAccounts(@QueryParam("userId") UUID userId);

    @POST
    Response createAccount(AccountDTO request);

    @PUT
    @Path("/{accountId}")
    Response updateAccount(@PathParam("accountId") UUID accountId, AccountDTO request);

    @DELETE
    @Path("/{accountId}")
    Response deleteAccount(@PathParam("accountId") UUID accountId, @QueryParam("userId") UUID userId);
}
