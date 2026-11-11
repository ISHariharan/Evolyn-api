package com.evolyn.api.Dashboard;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import com.evolyn.api.Common.DataServiceCall;

import jakarta.inject.Inject;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.executable.ValidateOnExecution;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * Public endpoints the dashboard page calls:
 *   GET /evolyn/api/dashboard/summary?userId=&from=&to=
 *   GET /evolyn/api/dashboard/trend?userId=&from=&to=
 */
@Path("/evolyn/api/dashboard")
@ValidateOnExecution
@Produces(MediaType.APPLICATION_JSON)
public class DashboardResource {

    // Keep queries bounded: at most ~2 years of data per request
    private static final long MAX_DAYS = 800;

    @Inject
    @RestClient
    DashboardStore dashboardStore;

    @GET
    @Path("/summary")
    public Response summary(@NotNull(message = "userId is required") @QueryParam("userId") UUID userId,
                            @QueryParam("from") String from,
                            @QueryParam("to") String to) {
        String problem = checkRange(from, to);
        if (problem != null) return badRequest(problem);
        return DataServiceCall.forward(() -> dashboardStore.summary(userId, from, to));
    }

    @GET
    @Path("/trend")
    public Response trend(@NotNull(message = "userId is required") @QueryParam("userId") UUID userId,
                          @QueryParam("from") String from,
                          @QueryParam("to") String to) {
        String problem = checkRange(from, to);
        if (problem != null) return badRequest(problem);
        return DataServiceCall.forward(() -> dashboardStore.trend(userId, from, to));
    }

    // Returns an error message, or null when the range is fine
    private static String checkRange(String from, String to) {
        if (from == null || to == null) return "from and to are required (YYYY-MM-DD)";
        try {
            LocalDate start = LocalDate.parse(from);
            LocalDate end = LocalDate.parse(to);
            if (end.isBefore(start)) return "to must be on or after from";
            if (ChronoUnit.DAYS.between(start, end) > MAX_DAYS) return "Date range is too long";
            return null;
        } catch (DateTimeParseException e) {
            return "from and to must be dates in YYYY-MM-DD format";
        }
    }

    private static Response badRequest(String message) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"message\":\"" + message + "\"}")
                .build();
    }
}
