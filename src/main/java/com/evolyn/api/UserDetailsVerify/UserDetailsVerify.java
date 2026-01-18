package com.evolyn.api.UserDetailsVerify;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("/evolyn/api/verify")
public class UserDetailsVerify {
        @POST
        @Path("/userdetails")
        public Response userDetailsVerify() {
            System.out.println("Received the data : ");
            return Response.ok("Success").build();
        }
}
