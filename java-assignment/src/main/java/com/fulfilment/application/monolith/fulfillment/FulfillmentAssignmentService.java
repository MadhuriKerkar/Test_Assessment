package com.fulfilment.application.monolith.fulfillment;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class FulfillmentAssignmentService {
    @PersistenceContext
    EntityManager em;

    @Transactional
    public void assignWarehouseToProductAndStore(Long productId, Long storeId, Long warehouseId) {
        // 1. Each Product can be fulfilled by max 2 Warehouses per Store
        Long countProductWarehouse = em.createQuery(
            "SELECT COUNT(f) FROM FulfillmentAssignment f WHERE f.product.id = :productId AND f.store.id = :storeId",
            Long.class)
            .setParameter("productId", productId)
            .setParameter("storeId", storeId)
            .getSingleResult();
        if (countProductWarehouse >= 2) {
            throw new IllegalArgumentException("A product can be fulfilled by max 2 warehouses per store");
        }
        // 2. Each Store can be fulfilled by max 3 Warehouses
        Long countStoreWarehouses = em.createQuery(
            "SELECT COUNT(DISTINCT f.warehouse.id) FROM FulfillmentAssignment f WHERE f.store.id = :storeId",
            Long.class)
            .setParameter("storeId", storeId)
            .getSingleResult();
        if (countStoreWarehouses >= 3) {
            throw new IllegalArgumentException("A store can be fulfilled by max 3 warehouses");
        }
        // 3. Each Warehouse can store max 5 types of Products
        Long countWarehouseProducts = em.createQuery(
            "SELECT COUNT(DISTINCT f.product.id) FROM FulfillmentAssignment f WHERE f.warehouse.id = :warehouseId",
            Long.class)
            .setParameter("warehouseId", warehouseId)
            .getSingleResult();
        if (countWarehouseProducts >= 5) {
            throw new IllegalArgumentException("A warehouse can store max 5 types of products");
        }
        // If all constraints pass, create assignment
        FulfillmentAssignment assignment = new FulfillmentAssignment();
        assignment.product = em.getReference(com.fulfilment.application.monolith.products.Product.class, productId);
        assignment.store = em.getReference(com.fulfilment.application.monolith.stores.Store.class, storeId);
        assignment.warehouse = em.getReference(com.fulfilment.application.monolith.warehouses.adapters.database.DbWarehouse.class, warehouseId);
        em.persist(assignment);
    }

    public List<FulfillmentAssignment> getAssignmentsForProductAndStore(Long productId, Long storeId) {
        return em.createQuery("SELECT f FROM FulfillmentAssignment f WHERE f.product.id = :productId AND f.store.id = :storeId", FulfillmentAssignment.class)
                .setParameter("productId", productId)
                .setParameter("storeId", storeId)
                .getResultList();
    }

    public List<FulfillmentAssignment> getAssignmentsForStore(Long storeId) {
        return em.createQuery("SELECT f FROM FulfillmentAssignment f WHERE f.store.id = :storeId", FulfillmentAssignment.class)
                .setParameter("storeId", storeId)
                .getResultList();
    }

    public List<FulfillmentAssignment> getAssignmentsForWarehouse(Long warehouseId) {
        return em.createQuery("SELECT f FROM FulfillmentAssignment f WHERE f.warehouse.id = :warehouseId", FulfillmentAssignment.class)
                .setParameter("warehouseId", warehouseId)
                .getResultList();
    }
}
