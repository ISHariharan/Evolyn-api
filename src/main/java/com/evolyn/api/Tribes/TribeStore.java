package com.evolyn.api.Tribes;

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

/** REST client for the Data Service's tribe endpoints (userId = the logged-in person). */
@Path("/evolyn/api/internal")
@RegisterRestClient(configKey = "evolyn-data-service")
@Consumes(MediaType.APPLICATION_JSON)
public interface TribeStore {

    @POST @Path("/tribes")
    Response create(@QueryParam("userId") UUID userId, TribeDTO body);

    @GET @Path("/tribes")
    Response mine(@QueryParam("userId") UUID userId);

    @GET @Path("/tribes/{tribeId}")
    Response detail(@PathParam("tribeId") UUID tribeId, @QueryParam("userId") UUID userId);

    @PUT @Path("/tribes/{tribeId}")
    Response update(@PathParam("tribeId") UUID tribeId, @QueryParam("userId") UUID userId, TribeDTO body);

    @POST @Path("/tribes/{tribeId}/archive")
    Response archive(@PathParam("tribeId") UUID tribeId, @QueryParam("userId") UUID userId);

    @POST @Path("/tribes/{tribeId}/leave")
    Response leave(@PathParam("tribeId") UUID tribeId, @QueryParam("userId") UUID userId);

    @POST @Path("/tribes/{tribeId}/members/{memberUserId}/remove")
    Response remove(@PathParam("tribeId") UUID tribeId, @PathParam("memberUserId") UUID memberUserId,
                    @QueryParam("userId") UUID userId);

    @POST @Path("/tribes/{tribeId}/invitations")
    Response invite(@PathParam("tribeId") UUID tribeId, @QueryParam("userId") UUID userId, InviteDTO body);

    @POST @Path("/tribes/{tribeId}/join-code")
    Response newJoinCode(@PathParam("tribeId") UUID tribeId, @QueryParam("userId") UUID userId);

    @DELETE @Path("/tribes/{tribeId}/join-code")
    Response joinCodeOff(@PathParam("tribeId") UUID tribeId, @QueryParam("userId") UUID userId);

    @GET @Path("/tribes/{tribeId}/feed")
    Response feed(@PathParam("tribeId") UUID tribeId, @QueryParam("userId") UUID userId,
                  @QueryParam("before") String before, @QueryParam("limit") Integer limit);

    @GET @Path("/invitations")
    Response myInvitations(@QueryParam("userId") UUID userId);

    @POST @Path("/invitations/{invitationId}/{action}")
    Response invitationAction(@PathParam("invitationId") UUID invitationId, @PathParam("action") String action,
                              @QueryParam("userId") UUID userId);

    @GET @Path("/tribe-codes/{code}")
    Response previewCode(@PathParam("code") String code, @QueryParam("userId") UUID userId);

    @POST @Path("/tribe-codes/{code}/join")
    Response joinByCode(@PathParam("code") String code, @QueryParam("userId") UUID userId);

    @GET @Path("/profiles/{profileId}")
    Response profile(@PathParam("profileId") String profileId);
}
