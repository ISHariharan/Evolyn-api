package com.evolyn.api.UserDetailsStore;

import jakarta.ws.rs.core.MediaType;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;

@Path("/evolyn/api/store")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserDetailsStore {
    @POST
    @Path("/userdetails")
    public Response userDetailsStore(@Valid UserDetailsStoreRequest Request){
        System.out.println("Received the data : ");
        return Response.ok("Success").build();
    }
}
