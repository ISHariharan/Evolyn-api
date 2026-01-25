package com.evolyn.api.UserDetailsVerify;

import java.util.Map;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.executable.ValidateOnExecution;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/evolyn/api/verify")
@ValidateOnExecution
public class UserDetailsVerify {
        @Inject
        @RestClient
        UserDetailsVerifyStore userDetailsVerifyStore;


        @POST
        @Path("/userdetails")
        public Response userDetailsVerify(@Valid UserDetailsVerifyValidation Request) {
            System.out.println("Received the data : " + Request.getEmail());
            System.out.println("Received the data : " + Request.getPassword());
            Response response = userDetailsVerifyStore.verifyUserDetails(Request);
            if(response.getStatus() > 400) {
                return Response
                        .status(response.getStatus())
                        .entity(response.readEntity(String.class))
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }
            return Response.ok(Map.of("message", "User Found")).build();
        }

        @POST
        @Path("/loggedin/user")
        public Response verifyUserDetails(@Valid VerifyLoggedinUserDTO Request) {
            System.out.println("Verify Logged In User : " + Request.getEmail());
            Response response =  userDetailsVerifyStore.verifyLoggedInUser(Request);

            if(response.getStatus() > 400) {
                return Response
                        .status(response.getStatus())
                        .entity(response.readEntity(String.class))
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }
            return Response
                    .status(response.getStatus())
                    .entity(response.readEntity(String.class))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
}
