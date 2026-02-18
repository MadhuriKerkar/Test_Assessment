package com.fulfilment.application.monolith.warehouses.adapters.restapi;

import jakarta.inject.Inject;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class WarehouseResourceImplTest {

    @Inject
    WarehouseResourceImpl resource;
    
    @Test
    @Transactional
    public void testListAllWarehousesUnits() {
        assertDoesNotThrow(() -> resource.listAllWarehousesUnits());
    }

    @Test
    @Transactional
    public void testCreateDelegatesAndReturnsResponse() {
        com.warehouse.api.beans.Warehouse warehouse = new com.warehouse.api.beans.Warehouse();
        warehouse.setBusinessUnitCode("RES_CREATE_SMOKE_BU");
        warehouse.setLocation("AMSTERDAM-001");
        warehouse.setCapacity(20);
        warehouse.setStock(10);
        var result = resource.createANewWarehouseUnit(warehouse);
        assertNotNull(result);
        assertEquals("RES_CREATE_SMOKE_BU", result.getBusinessUnitCode());
    }

    @Test
    @Transactional
    public void testReplaceDelegatesAndReturnsResponse() {
        var seed = new com.warehouse.api.beans.Warehouse();
        seed.setBusinessUnitCode("RES_REPLACE_SMOKE_BU");
        seed.setLocation("AMSTERDAM-001");
        seed.setCapacity(20);
        seed.setStock(20);
        resource.createANewWarehouseUnit(seed);

        // Replace with valid new warehouse details
        var replacement = new com.warehouse.api.beans.Warehouse();
        replacement.setBusinessUnitCode("RES_REPLACE_SMOKE_BU");
        replacement.setLocation("AMSTERDAM-001");
        replacement.setCapacity(20);
        replacement.setStock(20);
        var result = resource.replaceTheCurrentActiveWarehouse("RES_REPLACE_SMOKE_BU", replacement);
        assertNotNull(result);
        assertEquals("RES_REPLACE_SMOKE_BU", result.getBusinessUnitCode());
    }

    @Test
    @Transactional
    public void testGetAWarehouseUnitByIDThrowsForNotFound() {
        assertThrows(IllegalArgumentException.class, () -> resource.getAWarehouseUnitByID("NOT_FOUND"));
    }

    @Test
    @Transactional
    public void testGetAWarehouseUnitByIDReturnsWarehouse() {
        var result = resource.getAWarehouseUnitByID("MWH.012");
        assertNotNull(result);
        assertEquals("MWH.012", result.getBusinessUnitCode());
        assertEquals("AMSTERDAM-001", result.getLocation());
        assertEquals(50, result.getCapacity());
        assertEquals(5, result.getStock());
    }


    @Test
    @Transactional
    public void testArchiveAWarehouseUnitByIDThrowsForNotFound() {
        assertThrows(IllegalArgumentException.class, () -> resource.archiveAWarehouseUnitByID("NOT_FOUND"));
    }
	 
}
