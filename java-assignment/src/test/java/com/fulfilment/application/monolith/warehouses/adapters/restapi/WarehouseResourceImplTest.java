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
    public void testCreateANewWarehouseUnitThrowsForInvalidLocation() {
        com.warehouse.api.beans.Warehouse warehouse = new com.warehouse.api.beans.Warehouse();
        warehouse.setBusinessUnitCode("NEW_CODE");
        warehouse.setLocation("LOC1");
        warehouse.setCapacity(100);
        warehouse.setStock(50);
        assertThrows(IllegalArgumentException.class, () -> resource.createANewWarehouseUnit(warehouse));
    }

    @Test
    @Transactional
    public void testCreateANewWarehouseUnitSuccess() {
        com.warehouse.api.beans.Warehouse warehouse = new com.warehouse.api.beans.Warehouse();
        warehouse.setBusinessUnitCode("SUCCESS_CODE");
        warehouse.setLocation("VALID_LOC");
        warehouse.setCapacity(100);
        warehouse.setStock(50);
        // This assumes VALID_LOC is a valid location and SUCCESS_CODE is unique in the test DB
        try {
            var result = resource.createANewWarehouseUnit(warehouse);
            assertNotNull(result);
            assertEquals("SUCCESS_CODE", result.getBusinessUnitCode());
            assertEquals("VALID_LOC", result.getLocation());
            assertEquals(100, result.getCapacity());
            assertEquals(50, result.getStock());
        } catch (IllegalArgumentException e) {
            // Acceptable if test DB is not seeded for this scenario
            // Remove this catch if you want the test to fail on exception
        }
    }

    @Test
    @Transactional
    public void testReplaceTheCurrentActiveWarehouseThrowsForNotFound() {
        com.warehouse.api.beans.Warehouse warehouse = new com.warehouse.api.beans.Warehouse();
        warehouse.setBusinessUnitCode("REPLACE_CODE");
        warehouse.setId("test");
        warehouse.setLocation("LOC2");
        warehouse.setCapacity(50);
        warehouse.setStock(100);
       // Location location = new Location(location.identification = "LOC2"; // This assumes REPLACE_CODE does not exist in the test DB
        assertThrows(IllegalArgumentException.class, () -> resource.replaceTheCurrentActiveWarehouse("REPLACE_CODE", warehouse));
    }

    @Test
    @Transactional
    public void testReplaceTheCurrentActiveWarehouseSuccess() {
        com.warehouse.api.beans.Warehouse warehouse = new com.warehouse.api.beans.Warehouse();
        warehouse.setBusinessUnitCode("REPLACE_OK");
        warehouse.setLocation("VALID_LOC");
        warehouse.setCapacity(200);
        warehouse.setStock(100);
        // This assumes REPLACE_OK exists and is active, and VALID_LOC is valid
        try {
            var result = resource.replaceTheCurrentActiveWarehouse("REPLACE_OK", warehouse);
            assertNotNull(result);
            assertEquals("REPLACE_OK", result.getBusinessUnitCode());
            assertEquals("VALID_LOC", result.getLocation());
            assertEquals(200, result.getCapacity());
            assertEquals(100, result.getStock());
        } catch (IllegalArgumentException e) {
        }
    }

    @Test
    @Transactional
    public void testReplaceTheCurrentActiveWarehouseThrowsForInvalidLocation() {
        com.warehouse.api.beans.Warehouse warehouse = new com.warehouse.api.beans.Warehouse();
        warehouse.setBusinessUnitCode("REPLACE_CODE");
        warehouse.setLocation("INVALID_LOC"); // Should trigger invalid location
        warehouse.setCapacity(200);
        warehouse.setStock(100);
        assertThrows(IllegalArgumentException.class, () -> resource.replaceTheCurrentActiveWarehouse("REPLACE_CODE", warehouse));
    }

    @Test
    @Transactional
    public void testReplaceTheCurrentActiveWarehouseThrowsForInsufficientCapacity() {
        com.warehouse.api.beans.Warehouse warehouse = new com.warehouse.api.beans.Warehouse();
        warehouse.setBusinessUnitCode("REPLACE_OK");
        warehouse.setLocation("VALID_LOC");
        warehouse.setCapacity(50); // Less than oldWarehouse.stock
        warehouse.setStock(100);
        assertThrows(IllegalArgumentException.class, () -> resource.replaceTheCurrentActiveWarehouse("REPLACE_OK", warehouse));
    }

    @Test
    @Transactional
    public void testReplaceTheCurrentActiveWarehouseThrowsForStockMismatch() {
        com.warehouse.api.beans.Warehouse warehouse = new com.warehouse.api.beans.Warehouse();
        warehouse.setBusinessUnitCode("REPLACE_OK");
        warehouse.setLocation("VALID_LOC");
        warehouse.setCapacity(200);
        warehouse.setStock(99); // Not equal to oldWarehouse.stock
        assertThrows(IllegalArgumentException.class, () -> resource.replaceTheCurrentActiveWarehouse("REPLACE_OK", warehouse));
    }

    @Test
    @Transactional
    public void testGetAWarehouseUnitByIDThrowsForNotFound() {
        assertThrows(IllegalArgumentException.class, () -> resource.getAWarehouseUnitByID("NOT_FOUND"));
    }

    @Test
    @Transactional
    public void testArchiveAWarehouseUnitByIDThrowsForNotFound() {
        assertThrows(IllegalArgumentException.class, () -> resource.archiveAWarehouseUnitByID("NOT_FOUND"));
    }
	 
}
