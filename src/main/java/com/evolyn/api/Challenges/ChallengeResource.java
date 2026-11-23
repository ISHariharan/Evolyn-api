package com.evolyn.api.Challenges;

import java.util.UUID;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import com.evolyn.api.Common.DataServiceCall;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.executable.ValidateOnExecution;
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
 * Challenges (login token needed; members only; any member can create, publish or cancel):
 *   GET/POST /evolyn/api/tribes/{tribeId}/challenges
 *   GET/PUT  /evolyn/api/challenges/{id}
 *   POST     /evolyn/api/challenges/{id}/publish | cancel | leave
 *   POST     /evolyn/api/challenges/{id}/join      { "team": "A" } for team challenges
 *
 * Class path "/evolyn/api" like the other tribe resources.
 */
@Path("/evolyn/api")
@ValidateOnExecution
@Produces(MediaType.APPLICATION_JSON)
public class ChallengeResource {

    @Inject
    @RestClient
    ChallengeStore store;

    @GET @Path("/tribes/{tribeId}/challenges")
    public Response list(@PathParam("tribeId") UUID tribeId, @QueryParam("userId") UUID userId) {
        return DataServiceCall.forward(() -> store.list(tribeId, userId));
    }

    @POST @Path("/tribes/{tribeId}/challenges")
    public Response create(@PathParam("tribeId") UUID tribeId, @QueryParam("userId") UUID userId, @Valid ChallengeDTO body) {
        return DataServiceCall.forward(() -> store.create(tribeId, userId, body));
    }

    @GET @Path("/challenges/{challengeId}")
    public Response detail(@PathParam("challengeId") UUID challengeId, @QueryParam("userId") UUID userId) {
        return DataServiceCall.forward(() -> store.detail(challengeId, userId));
    }

    @PUT @Path("/challenges/{challengeId}")
    public Response update(@PathParam("challengeId") UUID challengeId, @QueryParam("userId") UUID userId, @Valid ChallengeDTO body) {
        return DataServiceCall.forward(() -> store.update(challengeId, userId, body));
    }

    @POST @Path("/challenges/{challengeId}/join")
    public Response join(@PathParam("challengeId") UUID challengeId, @QueryParam("userId") UUID userId, @Valid JoinDTO body) {
        return DataServiceCall.forward(() -> store.join(challengeId, userId, body == null ? new JoinDTO() : body));
    }

    @POST @Path("/challenges/{challengeId}/{action}")
    public Response action(@PathParam("challengeId") UUID challengeId,
                           @Pattern(regexp = "publish|cancel|leave", message = "Unknown action") @PathParam("action") String action,
                           @QueryParam("userId") UUID userId) {
        return DataServiceCall.forward(() -> store.action(challengeId, action, userId));
    }
}
