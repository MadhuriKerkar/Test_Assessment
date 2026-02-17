package com.fulfilment.application.monolith.stores;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class ErrorMapperTest {

    @Test
    public void testToResponse_withWebApplicationException() {
        StoreResource.ErrorMapper errorMapper = new StoreResource.ErrorMapper();
        errorMapper.objectMapper = new ObjectMapper();
        WebApplicationException ex = new WebApplicationException("Test error", 404);

        Response response = errorMapper.toResponse(ex);
        assertEquals(404, response.getStatus());
        assertTrue(response.getEntity() instanceof ObjectNode);
        ObjectNode node = (ObjectNode) response.getEntity();
        assertEquals(ex.getClass().getName(), node.get("exceptionType").asText());
        assertEquals(404, node.get("code").asInt());
        assertEquals("Test error", node.get("error").asText());
    }

    @Test
    public void testToResponse_withGenericException() {
        StoreResource.ErrorMapper errorMapper = new StoreResource.ErrorMapper();
        errorMapper.objectMapper = new ObjectMapper();
        Exception ex = new Exception("Generic error");

        Response response = errorMapper.toResponse(ex);
        assertEquals(500, response.getStatus());
        assertTrue(response.getEntity() instanceof ObjectNode);
        ObjectNode node = (ObjectNode) response.getEntity();
        assertEquals(ex.getClass().getName(), node.get("exceptionType").asText());
        assertEquals(500, node.get("code").asInt());
        assertEquals("Generic error", node.get("error").asText());
    }

    @Test
    public void testToResponse_withNullMessage() {
        StoreResource.ErrorMapper errorMapper = new StoreResource.ErrorMapper();
        errorMapper.objectMapper = new ObjectMapper();
        Exception ex = new Exception();

        Response response = errorMapper.toResponse(ex);
        assertEquals(500, response.getStatus());
        assertTrue(response.getEntity() instanceof ObjectNode);
        ObjectNode node = (ObjectNode) response.getEntity();
        assertEquals(ex.getClass().getName(), node.get("exceptionType").asText());
        assertEquals(500, node.get("code").asInt());
        assertNull(node.get("error"));
    }
}
