package com.fulfilment.application.monolith.stores;

import jakarta.inject.Inject;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

@QuarkusTest
public class StoreResourceTest {

    @Inject
    StoreResource resource;

    @Test
    @Transactional
    public void testGetReturnsList() {
        List<Store> stores = resource.get();
        assertNotNull(stores);
    }

    @Test
    @Transactional
    public void testGetSingleThrowsForNotFound() {
        assertThrows(WebApplicationException.class, () -> resource.getSingle(999L));
    }

    @Test
    @Transactional
    public void testCreateThrowsForIdSet() {
        Store store = new Store("TestStore");
        store.id = 1L;
        assertThrows(WebApplicationException.class, () -> resource.create(store));
    }
    
    @Test
    @Transactional
    public void testCreateThrowsForIdNotSet() {
        Store store = new Store("TestStore");
        Response response = resource.create(store); 
        assertNotNull(response); 
        assertEquals(201, response.getStatus());
    }

    @Test
    @Transactional
    public void testUpdateThrowsForNameNull() {
        Store store = new Store("TestStore");
        store.name = null;
        assertThrows(WebApplicationException.class, () -> resource.update(1L, store));
    }

    @Test
    @Transactional
    public void testUpdateThrowsForNotFound() {
        Store store = new Store("TestStore");
        store.name = "TestStore";
        assertThrows(WebApplicationException.class, () -> resource.update(999L, store));
    }

    @Test
    @Transactional
    public void testPatchThrowsForNameNull() {
        Store store = new Store("TestStore");
        store.name = null;
        assertThrows(WebApplicationException.class, () -> resource.patch(1L, store));
    }
    
    @Test
    @Transactional
    public void testPatchForValidValues() {
        Store store = new Store("TestStore");
        store.id = 1L;
        store.name = "ABC";
        store.quantityProductsInStock = 1;
        Store result = resource.patch(1L, store); 
        assertNotNull(result); 
        assertEquals(result.id, 1L);
        assertEquals(result.name, "ABC");
    }
    
    @Test
    @Transactional
    public void testUpdateForValidValues() {
        Store store = new Store("TestStore");
        store.id = 1L;
        store.name = "ABC";
        store.quantityProductsInStock = 1;
        Store result = resource.update(1L, store); 
        assertNotNull(result); 
        assertEquals(result.id, 1L);
        assertEquals(result.name, "ABC");
    }

    @Test
    @Transactional
    public void testPatchThrowsForNotFound() {
        Store store = new Store("TestStore");
        store.name = "TestStore";
        assertThrows(WebApplicationException.class, () -> resource.patch(999L, store));
    }

    @Test
    @Transactional
    public void testDeleteThrowsForNotFound() {
        assertThrows(WebApplicationException.class, () -> resource.delete(999L));
    }
}
