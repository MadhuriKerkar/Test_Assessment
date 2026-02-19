package com.fulfilment.application.monolith.fulfillment;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import java.util.List;

@ApplicationScoped
public class FulfillmentAssignmentRepository {
    @Inject
    EntityManager em;

    public Long countProductWarehouse(Long productId, Long storeId) {
        return em.createQuery(
            "SELECT COUNT(f) FROM FulfillmentAssignment f WHERE f.product.id = :productId AND f.store.id = :storeId",
            Long.class)
            .setParameter("productId", productId)
            .setParameter("storeId", storeId)
            .getSingleResult();
    }

    public Long countStoreWarehouses(Long storeId) {
        return em.createQuery(
            "SELECT COUNT(DISTINCT f.warehouse.id) FROM FulfillmentAssignment f WHERE f.store.id = :storeId",
            Long.class)
            .setParameter("storeId", storeId)
            .getSingleResult();
    }

    public Long countWarehouseProducts(Long warehouseId) {
        return em.createQuery(
            "SELECT COUNT(DISTINCT f.product.id) FROM FulfillmentAssignment f WHERE f.warehouse.id = :warehouseId",
            Long.class)
            .setParameter("warehouseId", warehouseId)
            .getSingleResult();
    }

    public void persist(FulfillmentAssignment assignment) {
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

    public <T> T getReference(Class<T> clazz, Object id) {
        return em.getReference(clazz, id);
    }
}
