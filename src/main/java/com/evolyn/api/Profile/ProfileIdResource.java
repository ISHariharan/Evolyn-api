package com.evolyn.api.Profile;

import java.util.UUID;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import com.evolyn.api.Common.DataServiceCall;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.executable.ValidateOnExecution;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * Profile IDs, the public handle friends use to find each other:
 *   GET /evolyn/api/profile-ids/{profileId}/availability   is it free? (no login needed: used while signing up)
 *   PUT /evolyn/api/me/profile-id   { "profileId": "arun_k" }   choose yours (accounts made before Profile IDs)
 */
@Path("/evolyn/api")
@ValidateOnExecution
@Produces(MediaType.APPLICATION_JSON)
public class ProfileIdResource {

    @Inject
    @RestClient
    ProfileIdStore store;

    @GET
    @Path("/profile-ids/{profileId}/availability")
    public Response availability(@Pattern(regexp = "@?[A-Za-z0-9_]{1,40}", message = "Use only letters, numbers and _")
                                 @PathParam("profileId") String profileId) {
        return DataServiceCall.forward(() -> store.availability(profileId));
    }

    // userId is filled in by AuthFilter from the login token
    @PUT
    @Path("/me/profile-id")
    public Response setMine(@QueryParam("userId") UUID userId, @Valid ProfileIdDTO request) {
        return DataServiceCall.forward(() -> store.setProfileId(userId, request));
    }
}
