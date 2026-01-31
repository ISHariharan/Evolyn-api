package com.evolyn.api.Workspace.Retrieve;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;


import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/evolyn/api/internal")
@RegisterRestClient(configKey = "evolyn-data-service")
@Consumes(MediaType.APPLICATION_JSON)
public interface RetrieveWorkspaceStore {
    @GET
    @Path("/retrieve/workspaces")
    Response getAllWorkspacesForUser(@QueryParam("userId") String userId);
}
