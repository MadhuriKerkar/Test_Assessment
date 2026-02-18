package com.fulfilment.application.monolith.fulfillment;

import com.fulfilment.application.monolith.products.ProductRepository;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class FulfillmentAssignmentServiceTest {

    @Inject
    FulfillmentAssignmentService service;

    @Inject
    ProductRepository productRepository;

    @Test
    @Transactional
    public void testAssignWarehouseToProductAndStoreSucceeds() {
        // Uses seeded entities: productId=1, storeId=1, warehouseId=1 from import.sql
        assertDoesNotThrow(() -> service.assignWarehouseToProductAndStore(1L, 1L, 1L));
    }

    @Test
    @Transactional
    public void testAssignEnforcesProductStoreMaxTwoWarehouses() {
        // First two distinct warehouses for same product+store are allowed
        service.assignWarehouseToProductAndStore(1L, 1L, 1L);
        service.assignWarehouseToProductAndStore(1L, 1L, 2L);
        // Third distinct warehouse for same product+store should fail
        assertThrows(IllegalArgumentException.class,
            () -> service.assignWarehouseToProductAndStore(1L, 1L, 3L));
    }

    @Test
    @Transactional
    public void testAssignEnforcesStoreMaxThreeWarehouses() {
        // For storeId=2, assign three distinct warehouses across different products (seeded product ids 1,2,3)
        service.assignWarehouseToProductAndStore(1L, 2L, 1L);
        service.assignWarehouseToProductAndStore(2L, 2L, 2L);
        service.assignWarehouseToProductAndStore(3L, 2L, 3L);
        // Any further assignment for the same store should fail
        assertThrows(IllegalArgumentException.class,
            () -> service.assignWarehouseToProductAndStore(1L, 2L, 1L));
    }
}
