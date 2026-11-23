package com.evolyn.api.Profile;

import java.util.UUID;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/** REST client for the Data Service's Profile ID endpoints. */
@Path("/evolyn/api/internal")
@RegisterRestClient(configKey = "evolyn-data-service")
@Consumes(MediaType.APPLICATION_JSON)
public interface ProfileIdStore {

    @GET
    @Path("/profile-ids/{profileId}/availability")
    Response availability(@PathParam("profileId") String profileId);

    @PUT
    @Path("/users/{userId}/profile-id")
    Response setProfileId(@PathParam("userId") UUID userId, ProfileIdDTO request);
}
