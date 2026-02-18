package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.warehouses.domain.models.Location;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.CreateWarehouseOperation;
import com.fulfilment.application.monolith.warehouses.domain.ports.LocationResolver;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import jakarta.enterprise.context.ApplicationScoped;
import java.time.LocalDateTime;

@ApplicationScoped
public class CreateWarehouseUseCase implements CreateWarehouseOperation {

  private final WarehouseStore warehouseStore;
  private final LocationResolver locationResolver;

  public CreateWarehouseUseCase(WarehouseStore warehouseStore, LocationResolver locationResolver) {
    this.warehouseStore = warehouseStore;
    this.locationResolver = locationResolver;
  }

  @Override
  public void create(Warehouse warehouse) {
    // Business Unit Code Verification
    if (warehouseStore.findByBusinessUnitCode(warehouse.businessUnitCode) != null) {
      throw new IllegalArgumentException("Business unit code already exists");
    }

    // Location Validation
    Location location = locationResolver.resolveByIdentifier(warehouse.location);
    if (location == null) {
      throw new IllegalArgumentException("Invalid location");
    }

    // Warehouse Creation Feasibility
    long count = warehouseStore.getAll().stream()
        .filter(w -> w.location.equals(warehouse.location) && w.archivedAt == null)
        .count();
    if (count >= location.maxNumberOfWarehouses) {
      throw new IllegalArgumentException("Maximum number of warehouses reached for this location");
    }

    // Capacity and Stock Validation
    int totalCapacity = warehouseStore.getAll().stream()
        .filter(w -> w.location.equals(warehouse.location) && w.archivedAt == null)
        .mapToInt(w -> w.capacity != null ? w.capacity : 0)
        .sum();
    if (warehouse.capacity + totalCapacity > location.maxCapacity) {
      throw new IllegalArgumentException("Warehouse capacity exceeds location maximum");
    }
    if (warehouse.stock != null && warehouse.capacity != null && warehouse.stock > warehouse.capacity) {
      throw new IllegalArgumentException("Stock exceeds warehouse capacity");
    }

    warehouse.createdAt = LocalDateTime.now();
    warehouseStore.create(warehouse);
  }
}
