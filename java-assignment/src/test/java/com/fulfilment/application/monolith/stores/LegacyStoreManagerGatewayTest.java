package com.fulfilment.application.monolith.stores;

import org.junit.jupiter.api.Test;
import io.quarkus.test.junit.QuarkusTest;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class LegacyStoreManagerGatewayTest {

    @Test
    public void testCreateStoreOnLegacySystem() {
        LegacyStoreManagerGateway gateway = new LegacyStoreManagerGateway();
        Store store = new Store("TestStore");
        store.quantityProductsInStock = 10;
        assertDoesNotThrow(() -> gateway.createStoreOnLegacySystem(store));
    }

    @Test
    public void testUpdateStoreOnLegacySystem() {
        LegacyStoreManagerGateway gateway = new LegacyStoreManagerGateway();
        Store store = new Store("UpdateStore");
        store.quantityProductsInStock = 20;
        assertDoesNotThrow(() -> gateway.updateStoreOnLegacySystem(store));
    }
}
