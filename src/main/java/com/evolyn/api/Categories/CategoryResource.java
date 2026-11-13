package com.evolyn.api.Categories;

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
 *   GET    /evolyn/api/categories?userId=
 *   POST   /evolyn/api/categories
 *   PUT    /evolyn/api/categories/{categoryId}
 *   DELETE /evolyn/api/categories/{categoryId}?userId=&reassignTo=
 * The rules (unique names, moving transactions before delete, ...) live in the Data Service.
 */
@Path("/evolyn/api/categories")
@ValidateOnExecution
@Produces(MediaType.APPLICATION_JSON)
public class CategoryResource {

    @Inject
    @RestClient
    CategoryStore categoryStore;

    @GET
    public Response listCategories(@NotNull(message = "userId is required") @QueryParam("userId") UUID userId) {
        return DataServiceCall.forward(() -> categoryStore.listCategories(userId));
    }

    @POST
    public Response createCategory(@Valid CategoryDTO request) {
        return DataServiceCall.forward(() -> categoryStore.createCategory(request));
    }

    @PUT
    @Path("/{categoryId}")
    public Response updateCategory(@PathParam("categoryId") UUID categoryId, @Valid CategoryDTO request) {
        return DataServiceCall.forward(() -> categoryStore.updateCategory(categoryId, request));
    }

    @DELETE
    @Path("/{categoryId}")
    public Response deleteCategory(@PathParam("categoryId") UUID categoryId,
                                   @NotNull(message = "userId is required") @QueryParam("userId") UUID userId,
                                   @QueryParam("reassignTo") UUID reassignTo) {
        return DataServiceCall.forward(() -> categoryStore.deleteCategory(categoryId, userId, reassignTo));
    }
}
