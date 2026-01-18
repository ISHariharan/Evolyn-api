package com.evolyn.api.UserDetailsStore;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;

@Path("/evolyn/api/internal")
@RegisterRestClient(configKey="evolyn-data-service")
@Consumes(MediaType.APPLICATION_JSON)
public interface EvolynDataServiceAuthStore {

    @POST
    @Path("/store/userdetails")
    void storeUserDetails(UserDetailsStoreRequestValidation Request);
}
