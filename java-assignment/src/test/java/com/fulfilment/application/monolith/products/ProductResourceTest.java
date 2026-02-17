package com.fulfilment.application.monolith.products;

import jakarta.inject.Inject;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import jakarta.ws.rs.WebApplicationException;
import java.util.List;

@QuarkusTest
public class ProductResourceTest {

    @Inject
    ProductResource resource;

    @Test
    @Transactional
    public void testGetReturnsList() {
        List<Product> products = resource.get();
        assertNotNull(products);
    }

    @Test
    @Transactional
    public void testGetSingleThrowsForNotFound() {
        assertThrows(WebApplicationException.class, () -> resource.getSingle(999L));
    }

    @Test
    @Transactional
    public void testCreateThrowsForIdSet() {
        Product product = new Product();
        product.id = 1L;
        assertThrows(WebApplicationException.class, () -> resource.create(product));
    }

    @Test
    @Transactional
    public void testUpdateThrowsForNameNull() {
        Product product = new Product();
        product.name = null;
        assertThrows(WebApplicationException.class, () -> resource.update(1L, product));
    }

    @Test
    @Transactional
    public void testUpdateThrowsForNotFound() {
        Product product = new Product();
        product.name = "Test";
        assertThrows(WebApplicationException.class, () -> resource.update(999L, product));
    }

    @Test
    @Transactional
    public void testDeleteThrowsForNotFound() {
        assertThrows(WebApplicationException.class, () -> resource.delete(999L));
    }
}
