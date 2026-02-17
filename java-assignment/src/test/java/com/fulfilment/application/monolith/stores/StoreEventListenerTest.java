package com.fulfilment.application.monolith.stores;

import org.junit.jupiter.api.Test;
import io.quarkus.test.junit.QuarkusTest;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class StoreEventListenerTest {
    @Test
    public void testOnStoreEventCreate() {
        StoreEventListener listener = new StoreEventListener();
        listener.legacyStoreManagerGateway = new LegacyStoreManagerGateway();
        Store store = new Store("TestStore");
        StoreEvent event = new StoreEvent(store, StoreEvent.StoreEventType.CREATE);
        assertDoesNotThrow(() -> listener.onStoreEvent(event));
    }

    @Test
    public void testOnStoreEventUpdate() {
        StoreEventListener listener = new StoreEventListener();
        listener.legacyStoreManagerGateway = new LegacyStoreManagerGateway();
        Store store = new Store("UpdateStore");
        StoreEvent event = new StoreEvent(store, StoreEvent.StoreEventType.UPDATE);
        assertDoesNotThrow(() -> listener.onStoreEvent(event));
    }
}
