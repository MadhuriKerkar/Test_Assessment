package com.fulfilment.application.monolith.common;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

@Provider
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {
    @Inject
    ObjectMapper objectMapper;
    private static final Logger LOGGER = Logger.getLogger(ConstraintViolationExceptionMapper.class);

    @Override
    public Response toResponse(ConstraintViolationException exception) {
        LOGGER.warn("Validation failed", exception);
        ObjectNode errorJson = objectMapper.createObjectNode();
        errorJson.put("exceptionType", exception.getClass().getName());
        errorJson.put("code", 400);
        errorJson.put("error", "Validation failed");
        var details = objectMapper.createArrayNode();
        for (ConstraintViolation<?> v : exception.getConstraintViolations()) {
            details.add(v.getPropertyPath() + ": " + v.getMessage());
        }
        errorJson.set("details", details);
        return Response.status(400).entity(errorJson).build();
    }
}
