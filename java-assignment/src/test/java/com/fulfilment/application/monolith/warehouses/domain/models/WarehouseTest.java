package com.fulfilment.application.monolith.warehouses.domain.models;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class WarehouseTest {
    @Test
    public void testGetters() {
        Warehouse warehouse = new Warehouse();
        warehouse.businessUnitCode = "BU123";
        warehouse.location = "Berlin";
        warehouse.capacity = 1000;
        warehouse.stock = 500;
        LocalDateTime now = LocalDateTime.now();
        warehouse.createdAt = now;
        warehouse.archivedAt = now.plusDays(1);

        assertEquals("BU123", warehouse.businessUnitCode);
        assertEquals("Berlin", warehouse.location);
        assertEquals(1000, warehouse.capacity);
        assertEquals(500, warehouse.stock);
        assertEquals(now, warehouse.createdAt);
        assertEquals(now.plusDays(1), warehouse.archivedAt);
    }
}
