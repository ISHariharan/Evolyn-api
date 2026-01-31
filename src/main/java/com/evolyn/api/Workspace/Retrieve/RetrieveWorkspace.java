package com.evolyn.api.Workspace.Retrieve;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import jakarta.inject.Inject;
import jakarta.validation.executable.ValidateOnExecution;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/evolyn/api")
@ValidateOnExecution
public class RetrieveWorkspace {
    @Inject
    @RestClient
    RetrieveWorkspaceStore retrieveWorkspaceStore;


    @GET
    @Path("/retrieve/workspaces")
    public Response RetrieveUserWorkspaces(@QueryParam("userId") String userId) {
        try {
            if (userId == null || userId.isBlank()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"message\":\"userId is required\"}")
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }
            Response response = retrieveWorkspaceStore.getAllWorkspacesForUser(userId);
            return Response.status(response.getStatus())
                    .entity(response.readEntity(String.class))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        } catch (Exception e) {
            System.out.println("Request to Server Denied " + e);
            return Response.serverError()
                    .entity("{\"message\":\"Request to Server Denied\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
    }
}
