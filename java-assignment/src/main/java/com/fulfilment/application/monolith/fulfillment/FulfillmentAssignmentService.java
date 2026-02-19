package com.fulfilment.application.monolith.fulfillment;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class FulfillmentAssignmentService {

    @Inject
    FulfillmentAssignmentRepository repository;

    @Transactional
    public void assignWarehouseToProductAndStore(Long productId, Long storeId, Long warehouseId) {
        // 1. Each Product can be fulfilled by max 2 Warehouses per Store
        Long countProductWarehouse = repository.countProductWarehouse(productId, storeId);
        if (countProductWarehouse >= 2) {
            throw new IllegalArgumentException("A product can be fulfilled by max 2 warehouses per store");
        }
        // 2. Each Store can be fulfilled by max 3 Warehouses
        Long countStoreWarehouses = repository.countStoreWarehouses(storeId);
        if (countStoreWarehouses >= 3) {
            throw new IllegalArgumentException("A store can be fulfilled by max 3 warehouses");
        }
        // 3. Each Warehouse can store max 5 types of Products
        Long countWarehouseProducts = repository.countWarehouseProducts(warehouseId);
        if (countWarehouseProducts >= 5) {
            throw new IllegalArgumentException("A warehouse can store max 5 types of products");
        }
        // If all constraints pass, create assignment
        FulfillmentAssignment assignment = new FulfillmentAssignment();
        assignment.product = repository.getReference(com.fulfilment.application.monolith.products.Product.class, productId);
        assignment.store = repository.getReference(com.fulfilment.application.monolith.stores.Store.class, storeId);
        assignment.warehouse = repository.getReference(com.fulfilment.application.monolith.warehouses.adapters.database.DbWarehouse.class, warehouseId);
        repository.persist(assignment);
    }

    public List<FulfillmentAssignment> getAssignmentsForProductAndStore(Long productId, Long storeId) {
        return repository.getAssignmentsForProductAndStore(productId, storeId);
    }

    public List<FulfillmentAssignment> getAssignmentsForStore(Long storeId) {
        return repository.getAssignmentsForStore(storeId);
    }

    public List<FulfillmentAssignment> getAssignmentsForWarehouse(Long warehouseId) {
        return repository.getAssignmentsForWarehouse(warehouseId);
    }
}
