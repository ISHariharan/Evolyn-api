package com.evolyn.api.Password;

import java.util.UUID;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import com.evolyn.api.Common.DataServiceCall;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.executable.ValidateOnExecution;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * Recovery codes, forgotten passwords and changing your password:
 *   GET  /evolyn/api/recovery-code                          do you have a recovery code? (login needed)
 *   POST /evolyn/api/recovery-code   { currentPassword }    make a new one, shown once (login needed)
 *   POST /evolyn/api/password-reset  { email, recoveryCode, password }   (public) answers with a new code
 *   POST /evolyn/api/password/change { currentPassword, newPassword }    (login needed)
 */
@Path("/evolyn/api")
@ValidateOnExecution
@Produces(MediaType.APPLICATION_JSON)
public class PasswordResource {

    @Inject
    @RestClient
    PasswordStore store;

    @GET @Path("/recovery-code")
    public Response codeStatus(@QueryParam("userId") UUID userId) {
        return DataServiceCall.forward(() -> store.codeStatus(userId));
    }

    @POST @Path("/recovery-code")
    public Response createCode(@QueryParam("userId") UUID userId, @Valid PasswordDTOs.NewCode body) {
        return DataServiceCall.forward(() -> store.createCode(userId, body));
    }

    @POST @Path("/password-reset")
    public Response reset(@Valid PasswordDTOs.Reset body) {
        return DataServiceCall.forward(() -> store.reset(body));
    }

    @POST @Path("/password/change")
    public Response change(@QueryParam("userId") UUID userId, @Valid PasswordDTOs.Change body) {
        return DataServiceCall.forward(() -> store.change(userId, body));
    }
}
