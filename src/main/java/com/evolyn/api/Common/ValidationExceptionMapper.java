package com.evolyn.api.Common;

import java.util.List;
import java.util.Map;

import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/**
 * When @Valid fails, Helidon throws ConstraintViolationException.
 * This mapper turns it into a clean JSON 400 the frontend can display:
 *   { "message": "Validation failed", "errors": ["amount must be greater than 0", ...] }
 * Applies to every endpoint in Evolyn-API, not just transactions.
 *
 * @Priority(1): Helidon has its own mapper for the same exception (plain-text 400).
 * Without a priority, which one runs could change from one start-up to the next.
 */
@Provider
@Priority(1)
@ApplicationScoped
public class ValidationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(ConstraintViolationException exception) {
        List<String> errors = exception.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .sorted()
                .toList();

        return Response.status(Response.Status.BAD_REQUEST)
                .type(MediaType.APPLICATION_JSON)
                .entity(Map.of("message", "Validation failed", "errors", errors))
                .build();
    }
}
