package com.evolyn.api.UserDetailsVerify;

import java.io.StringReader;
import java.util.Map;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import com.evolyn.api.Auth.TokenService;
import com.evolyn.api.Common.DataServiceCall;

import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.validation.Valid;
import jakarta.validation.executable.ValidateOnExecution;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * POST /evolyn/api/verify/userdetails   (login, one of the few endpoints that needs no token)
 *
 * On success it answers with a signed login token and the user's details:
 *   { "message", "token", "expiresAt", "user": { "id", "email", "firstName", "lastName" } }
 * The browser sends the token as "Authorization: Bearer <token>" on every later
 * request, and AuthFilter takes the user id from it.
 *
 * (The old "/verify/loggedin/user" endpoint, which handed out any user's id for
 * their email address, is gone; the browser asks GET /evolyn/api/me instead.)
 */
@Path("/evolyn/api/verify")
@ValidateOnExecution
@Produces(MediaType.APPLICATION_JSON)
public class UserDetailsVerify {

    @Inject
    @RestClient
    UserDetailsVerifyStore userDetailsVerifyStore;

    @Inject
    TokenService tokens;

    @POST
    @Path("/userdetails")
    public Response login(@Valid UserDetailsVerifyValidation request) {
        // Never log the password (or the email: it's personal data)
        Response upstream = DataServiceCall.forward(() -> userDetailsVerifyStore.verifyUserDetails(request));
        if (upstream.getStatus() != 200) {
            return upstream; // wrong email/password, or the Data Service is down
        }
        JsonObject user;
        try (JsonReader reader = Json.createReader(new StringReader((String) upstream.getEntity()))) {
            user = reader.readObject();
        }
        TokenService.Issued issued = tokens.issue(java.util.UUID.fromString(user.getString("id")));
        return Response.ok(Map.of(
                "message", "User Found",
                "token", issued.token(),
                "expiresAt", issued.expiresAt().toString(),
                "user", Map.of(
                        "id", user.getString("id"),
                        "email", user.getString("email", ""),
                        "firstName", user.getString("firstName", ""),
                        "lastName", user.getString("lastName", ""))))
                .build();
    }
}
