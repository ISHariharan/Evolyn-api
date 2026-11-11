package com.evolyn.api.Transactions;

import java.util.UUID;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import com.evolyn.api.Common.DataServiceCall;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.executable.ValidateOnExecution;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
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
 *   POST   /evolyn/api/transactions
 *   GET    /evolyn/api/transactions?userId=&type=&category=&accountId=&from=&to=&page=&size=
 *   GET    /evolyn/api/transactions/{id}?userId=
 *   PUT    /evolyn/api/transactions/{id}
 *   DELETE /evolyn/api/transactions/{id}?userId=
 */
@Path("/evolyn/api/transactions")
@ValidateOnExecution
@Produces(MediaType.APPLICATION_JSON)
public class TransactionResource {

    private static final String ISO_DATE = "^\\d{4}-\\d{2}-\\d{2}$";

    @Inject
    @RestClient
    TransactionStore transactionStore;

    @POST
    public Response createTransaction(@Valid TransactionDTO request) {
        // If @Valid fails, ValidationExceptionMapper answers 400 before this line runs.
        return DataServiceCall.forward(() -> transactionStore.createTransaction(request));
    }

    @GET
    public Response listTransactions(
            @NotNull(message = "userId is required") @QueryParam("userId") UUID userId,
            @Pattern(regexp = "EXPENSE|INCOME", message = "type must be EXPENSE or INCOME") @QueryParam("type") String type,
            @QueryParam("category") String category,
            @QueryParam("accountId") UUID accountId,
            @Pattern(regexp = ISO_DATE, message = "from must be YYYY-MM-DD") @QueryParam("from") String from,
            @Pattern(regexp = ISO_DATE, message = "to must be YYYY-MM-DD") @QueryParam("to") String to,
            @Min(value = 0, message = "page must be 0 or more") @DefaultValue("0") @QueryParam("page") Integer page,
            @Min(value = 1, message = "size must be 1-100") @Max(value = 100, message = "size must be 1-100")
            @DefaultValue("10") @QueryParam("size") Integer size) {
        return DataServiceCall.forward(() ->
                transactionStore.listTransactions(userId, type, category, accountId, from, to, page, size));
    }

    @GET
    @Path("/{transactionId}")
    public Response getTransaction(@PathParam("transactionId") UUID transactionId,
                                   @NotNull(message = "userId is required") @QueryParam("userId") UUID userId) {
        return DataServiceCall.forward(() -> transactionStore.getTransaction(transactionId, userId));
    }

    @PUT
    @Path("/{transactionId}")
    public Response updateTransaction(@PathParam("transactionId") UUID transactionId, @Valid TransactionDTO request) {
        return DataServiceCall.forward(() -> transactionStore.updateTransaction(transactionId, request));
    }

    @DELETE
    @Path("/{transactionId}")
    public Response deleteTransaction(@PathParam("transactionId") UUID transactionId,
                                      @NotNull(message = "userId is required") @QueryParam("userId") UUID userId) {
        return DataServiceCall.forward(() -> transactionStore.deleteTransaction(transactionId, userId));
    }
}
