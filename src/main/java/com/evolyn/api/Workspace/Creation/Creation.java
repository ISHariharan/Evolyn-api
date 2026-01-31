package com.evolyn.api.Workspace.Creation;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.executable.ValidateOnExecution;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/evolyn/api/store")
@ValidateOnExecution
public class Creation {
    @Inject
    @RestClient
    WorkspaceCreationStore workSpaceCreationStore;

    @POST
    @Path("/workspace/creation")
    public Response workspaceCreation(@Valid WorkspaceCreationDTO Request){
        System.out.println("Workspace Creation Response : " + Request.getWorkspaceDetails());
        Response response = workSpaceCreationStore.WorkspaceCreation(Request);
        return Response
                .status(response.getStatus())
                .entity(response.readEntity(String.class))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
