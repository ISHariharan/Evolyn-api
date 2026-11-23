package com.evolyn.api.Tribes;

import java.util.UUID;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import com.evolyn.api.Common.DataServiceCall;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
 * Tribes, invitations, invite links and the tribe feed. All need a login token;
 * userId is filled in by AuthFilter from it. Every member can do everything
 * for now (no roles); the Data Service checks membership on every call.
 *
 *   GET/POST /evolyn/api/tribes                              my tribes / create
 *   GET/PUT  /evolyn/api/tribes/{id}                          one tribe (members, invitations, link) / edit
 *   POST     /evolyn/api/tribes/{id}/archive | leave
 *   POST     /evolyn/api/tribes/{id}/members/{userId}/remove
 *   POST     /evolyn/api/tribes/{id}/invitations              invite { profileId }
 *   POST/DELETE /evolyn/api/tribes/{id}/join-code             new invite link / turn it off
 *   GET      /evolyn/api/tribes/{id}/feed?before=&limit=       members' shared activities
 *   GET      /evolyn/api/invitations                          waiting for me
 *   POST     /evolyn/api/invitations/{id}/accept | decline | revoke
 *   GET/POST /evolyn/api/tribe-codes/{code}[/join]            preview / join with an invite link
 *   GET      /evolyn/api/profiles/{profileId}                 name behind a Profile ID
 */
@Path("/evolyn/api")
@ValidateOnExecution
@Produces(MediaType.APPLICATION_JSON)
public class TribeResource {

    private static final String CODE = "[A-Za-z0-9-]{4,16}";

    @Inject
    @RestClient
    TribeStore store;

    @GET @Path("/tribes")
    public Response mine(@QueryParam("userId") UUID userId) {
        return DataServiceCall.forward(() -> store.mine(userId));
    }

    @POST @Path("/tribes")
    public Response create(@QueryParam("userId") UUID userId, @Valid TribeDTO body) {
        return DataServiceCall.forward(() -> store.create(userId, body));
    }

    @GET @Path("/tribes/{tribeId}")
    public Response detail(@PathParam("tribeId") UUID tribeId, @QueryParam("userId") UUID userId) {
        return DataServiceCall.forward(() -> store.detail(tribeId, userId));
    }

    @PUT @Path("/tribes/{tribeId}")
    public Response update(@PathParam("tribeId") UUID tribeId, @QueryParam("userId") UUID userId, @Valid TribeDTO body) {
        return DataServiceCall.forward(() -> store.update(tribeId, userId, body));
    }

    @POST @Path("/tribes/{tribeId}/archive")
    public Response archive(@PathParam("tribeId") UUID tribeId, @QueryParam("userId") UUID userId) {
        return DataServiceCall.forward(() -> store.archive(tribeId, userId));
    }

    @POST @Path("/tribes/{tribeId}/leave")
    public Response leave(@PathParam("tribeId") UUID tribeId, @QueryParam("userId") UUID userId) {
        return DataServiceCall.forward(() -> store.leave(tribeId, userId));
    }

    @POST @Path("/tribes/{tribeId}/members/{memberUserId}/remove")
    public Response remove(@PathParam("tribeId") UUID tribeId, @PathParam("memberUserId") UUID memberUserId,
                           @QueryParam("userId") UUID userId) {
        return DataServiceCall.forward(() -> store.remove(tribeId, memberUserId, userId));
    }

    @POST @Path("/tribes/{tribeId}/invitations")
    public Response invite(@PathParam("tribeId") UUID tribeId, @QueryParam("userId") UUID userId, @Valid InviteDTO body) {
        return DataServiceCall.forward(() -> store.invite(tribeId, userId, body));
    }

    @POST @Path("/tribes/{tribeId}/join-code")
    public Response newJoinCode(@PathParam("tribeId") UUID tribeId, @QueryParam("userId") UUID userId) {
        return DataServiceCall.forward(() -> store.newJoinCode(tribeId, userId));
    }

    @DELETE @Path("/tribes/{tribeId}/join-code")
    public Response joinCodeOff(@PathParam("tribeId") UUID tribeId, @QueryParam("userId") UUID userId) {
        return DataServiceCall.forward(() -> store.joinCodeOff(tribeId, userId));
    }

    @GET @Path("/tribes/{tribeId}/feed")
    public Response feed(@PathParam("tribeId") UUID tribeId, @QueryParam("userId") UUID userId,
                         @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}T[0-9:.]+Z$", message = "before must be a UTC timestamp")
                         @QueryParam("before") String before,
                         @Min(value = 1, message = "limit must be 1-50") @Max(value = 50, message = "limit must be 1-50")
                         @DefaultValue("20") @QueryParam("limit") Integer limit) {
        return DataServiceCall.forward(() -> store.feed(tribeId, userId, before, limit));
    }

    @GET @Path("/invitations")
    public Response myInvitations(@QueryParam("userId") UUID userId) {
        return DataServiceCall.forward(() -> store.myInvitations(userId));
    }

    @POST @Path("/invitations/{invitationId}/{action}")
    public Response invitationAction(@PathParam("invitationId") UUID invitationId,
                                     @Pattern(regexp = "accept|decline|revoke", message = "Unknown action")
                                     @PathParam("action") String action,
                                     @QueryParam("userId") UUID userId) {
        return DataServiceCall.forward(() -> store.invitationAction(invitationId, action, userId));
    }

    @GET @Path("/tribe-codes/{code}")
    public Response previewCode(@Pattern(regexp = CODE, message = "Invalid invite code") @PathParam("code") String code,
                                @QueryParam("userId") UUID userId) {
        return DataServiceCall.forward(() -> store.previewCode(code, userId));
    }

    @POST @Path("/tribe-codes/{code}/join")
    public Response joinByCode(@Pattern(regexp = CODE, message = "Invalid invite code") @PathParam("code") String code,
                               @QueryParam("userId") UUID userId) {
        return DataServiceCall.forward(() -> store.joinByCode(code, userId));
    }

    @GET @Path("/profiles/{profileId}")
    public Response profile(@Pattern(regexp = "@?[A-Za-z0-9_]{3,20}", message = "Profile ID: 3 to 20 letters, numbers or _")
                            @PathParam("profileId") String profileId) {
        return DataServiceCall.forward(() -> store.profile(profileId));
    }
}
