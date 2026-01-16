package com.evolyn.api.Verify;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("/evolyn/api/verify")
public class UserDetails {
        @POST
        @Path("/userdetails")
        public Response test() {
            System.out.println("Received the data : ");
            return Response.ok("Success").build();
        }
}
