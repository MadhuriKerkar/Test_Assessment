package com.fulfilment.application.monolith.stores;

import org.junit.jupiter.api.Test;
import io.quarkus.test.junit.QuarkusTest;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class StoreEventTest {
    @Test
    public void testStoreEventCreate() {
        Store store = new Store("TestStore");
        StoreEvent event = new StoreEvent(store, StoreEvent.StoreEventType.CREATE);
        assertEquals(store, event.store);
        assertEquals(StoreEvent.StoreEventType.CREATE, event.type);
    }

    @Test
    public void testStoreEventUpdate() {
        Store store = new Store("UpdateStore");
        StoreEvent event = new StoreEvent(store, StoreEvent.StoreEventType.UPDATE);
        assertEquals(store, event.store);
        assertEquals(StoreEvent.StoreEventType.UPDATE, event.type);
    }
}
