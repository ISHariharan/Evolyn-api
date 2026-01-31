package com.evolyn.api.Workspace.Creation;


import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/evolyn/api/internal")
@RegisterRestClient(configKey = "evolyn-data-service")
@Consumes(MediaType.APPLICATION_JSON)
public interface WorkspaceCreationStore {

    @POST
    @Path("/store/workspace/creation")
    public Response WorkspaceCreation(WorkspaceCreationDTO Request);
}
