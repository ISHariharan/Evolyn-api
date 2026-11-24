package com.evolyn.api.Challenges;

import java.util.UUID;

import com.evolyn.api.Common.DataServiceRetry;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/** REST client for the Data Service's challenge endpoints. */
@Path("/evolyn/api/internal")
@DataServiceRetry
@RegisterRestClient(configKey = "evolyn-data-service")
@Consumes(MediaType.APPLICATION_JSON)
public interface ChallengeStore {

    @GET @Path("/tribes/{tribeId}/challenges")
    Response list(@PathParam("tribeId") UUID tribeId, @QueryParam("userId") UUID userId);

    @POST @Path("/tribes/{tribeId}/challenges")
    Response create(@PathParam("tribeId") UUID tribeId, @QueryParam("userId") UUID userId, ChallengeDTO body);

    @GET @Path("/challenges/{challengeId}")
    Response detail(@PathParam("challengeId") UUID challengeId, @QueryParam("userId") UUID userId);

    @PUT @Path("/challenges/{challengeId}")
    Response update(@PathParam("challengeId") UUID challengeId, @QueryParam("userId") UUID userId, ChallengeDTO body);

    @POST @Path("/challenges/{challengeId}/{action}")
    Response action(@PathParam("challengeId") UUID challengeId, @PathParam("action") String action,
                    @QueryParam("userId") UUID userId);

    @POST @Path("/challenges/{challengeId}/join")
    Response join(@PathParam("challengeId") UUID challengeId, @QueryParam("userId") UUID userId, JoinDTO body);
}
