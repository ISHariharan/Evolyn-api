package com.evolyn.api.Transactions;

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

/**
 * MicroProfile REST Client: an interface describing the Data Service's endpoints.
 * Helidon generates the HTTP code; the base URL comes from
 * evolyn-data-service/mp-rest/url in microprofile-config.properties.
 * Null @QueryParam values are simply left out of the URL.
 */
@Path("/evolyn/api/internal/transactions")
@RegisterRestClient(configKey = "evolyn-data-service")
@Consumes(MediaType.APPLICATION_JSON)
public interface TransactionStore {

    @POST
    Response createTransaction(TransactionDTO request);

    @GET
    Response listTransactions(@QueryParam("userId") UUID userId,
                              @QueryParam("type") String type,
                              @QueryParam("categoryId") UUID categoryId,
                              @QueryParam("accountId") UUID accountId,
                              @QueryParam("from") String from,
                              @QueryParam("to") String to,
                              @QueryParam("page") Integer page,
                              @QueryParam("size") Integer size);

    @GET
    @Path("/{transactionId}")
    Response getTransaction(@PathParam("transactionId") UUID transactionId, @QueryParam("userId") UUID userId);

    @PUT
    @Path("/{transactionId}")
    Response updateTransaction(@PathParam("transactionId") UUID transactionId, TransactionDTO request);

    @DELETE
    @Path("/{transactionId}")
    Response deleteTransaction(@PathParam("transactionId") UUID transactionId, @QueryParam("userId") UUID userId);
}
