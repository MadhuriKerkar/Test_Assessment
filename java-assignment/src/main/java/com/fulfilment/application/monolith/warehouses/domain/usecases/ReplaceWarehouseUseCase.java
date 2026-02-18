package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.warehouses.domain.models.Location;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.LocationResolver;
import com.fulfilment.application.monolith.warehouses.domain.ports.ReplaceWarehouseOperation;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import jakarta.enterprise.context.ApplicationScoped;
import java.time.LocalDateTime;

@ApplicationScoped
public class ReplaceWarehouseUseCase implements ReplaceWarehouseOperation {

  private final WarehouseStore warehouseStore;
  private final LocationResolver locationResolver;

  public ReplaceWarehouseUseCase(WarehouseStore warehouseStore, LocationResolver locationResolver) {
    this.warehouseStore = warehouseStore;
    this.locationResolver = locationResolver;
  }

  @Override
  public void replace(Warehouse newWarehouse) {
    // Find the warehouse to replace
    Warehouse oldWarehouse = warehouseStore.findByBusinessUnitCode(newWarehouse.businessUnitCode);
    if (oldWarehouse == null || oldWarehouse.archivedAt != null) {
      throw new IllegalArgumentException("Warehouse to replace not found or already archived");
    }

    // Location Validation
    Location location = locationResolver.resolveByIdentifier(newWarehouse.location);
    if (location == null) {
      throw new IllegalArgumentException("Invalid location");
    }

    // Capacity Accommodation
    if (newWarehouse.capacity != null && oldWarehouse.stock != null && newWarehouse.capacity < oldWarehouse.stock) {
      throw new IllegalArgumentException("New warehouse capacity cannot accommodate old stock");
    }

    // Stock Matching
    if (newWarehouse.stock == null || !newWarehouse.stock.equals(oldWarehouse.stock)) {
      throw new IllegalArgumentException("Stock of new warehouse must match old warehouse");
    }

    // Archive old warehouse
    oldWarehouse.archivedAt = LocalDateTime.now();
    warehouseStore.update(oldWarehouse);

    // Create new warehouse with same business unit code
    Warehouse replacement = new Warehouse();
    replacement.businessUnitCode = newWarehouse.businessUnitCode;
    replacement.location = newWarehouse.location;
    replacement.capacity = newWarehouse.capacity;
    replacement.stock = newWarehouse.stock;
    replacement.createdAt = LocalDateTime.now();
    warehouseStore.create(replacement);
  }
}
