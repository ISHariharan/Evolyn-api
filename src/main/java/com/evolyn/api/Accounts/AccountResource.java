package com.evolyn.api.Accounts;

import java.util.UUID;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import com.evolyn.api.Common.DataServiceCall;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.executable.ValidateOnExecution;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * Public endpoints the frontend calls:
 *   GET    /evolyn/api/accounts?userId=
 *   POST   /evolyn/api/accounts
 *   PUT    /evolyn/api/accounts/{accountId}
 *   DELETE /evolyn/api/accounts/{accountId}?userId=
 */
@Path("/evolyn/api/accounts")
@ValidateOnExecution
@Produces(MediaType.APPLICATION_JSON)
public class AccountResource {

    @Inject
    @RestClient
    AccountStore accountStore;

    @GET
    public Response listAccounts(@NotNull(message = "userId is required") @QueryParam("userId") UUID userId) {
        return DataServiceCall.forward(() -> accountStore.listAccounts(userId));
    }

    @POST
    public Response createAccount(@Valid AccountDTO request) {
        return DataServiceCall.forward(() -> accountStore.createAccount(request));
    }

    @PUT
    @Path("/{accountId}")
    public Response updateAccount(@PathParam("accountId") UUID accountId, @Valid AccountDTO request) {
        return DataServiceCall.forward(() -> accountStore.updateAccount(accountId, request));
    }

    @DELETE
    @Path("/{accountId}")
    public Response deleteAccount(@PathParam("accountId") UUID accountId,
                                  @NotNull(message = "userId is required") @QueryParam("userId") UUID userId) {
        return DataServiceCall.forward(() -> accountStore.deleteAccount(accountId, userId));
    }
}
