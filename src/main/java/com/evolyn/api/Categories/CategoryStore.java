package com.evolyn.api.Categories;

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

/** REST client for the Data Service's category endpoints. */
@Path("/evolyn/api/internal/categories")
@RegisterRestClient(configKey = "evolyn-data-service")
@Consumes(MediaType.APPLICATION_JSON)
public interface CategoryStore {

    @GET
    Response listCategories(@QueryParam("userId") UUID userId);

    @POST
    Response createCategory(CategoryDTO request);

    @PUT
    @Path("/{categoryId}")
    Response updateCategory(@PathParam("categoryId") UUID categoryId, CategoryDTO request);

    @DELETE
    @Path("/{categoryId}")
    Response deleteCategory(@PathParam("categoryId") UUID categoryId,
                            @QueryParam("userId") UUID userId,
                            @QueryParam("reassignTo") UUID reassignTo);
}
