package com.fulfilment.application.monolith.warehouses.adapters.restapi;

import com.fulfilment.application.monolith.warehouses.adapters.database.WarehouseRepository;
import com.fulfilment.application.monolith.location.LocationGateway;
import com.fulfilment.application.monolith.warehouses.domain.models.Location;
import com.warehouse.api.WarehouseResource;
import com.warehouse.api.beans.Warehouse;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@RequestScoped
public class WarehouseResourceImpl implements WarehouseResource {

	@Inject
	private WarehouseRepository warehouseRepository;
	@Inject
	private LocationGateway locationGateway;

	@Override
	public List<Warehouse> listAllWarehousesUnits() {
		return warehouseRepository.getAll().stream().map(this::toWarehouseResponse).toList();
	}

	@Override
	public Warehouse createANewWarehouseUnit(@NotNull Warehouse data) {
		// Business Unit Code Verification
		if (warehouseRepository.findByBusinessUnitCode(data.getBusinessUnitCode()) != null) {
			throw new IllegalArgumentException("Business unit code already exists");
		}
		// Location Validation
		Location location = locationGateway.resolveByIdentifier(data.getLocation());
		if (location == null) {
			throw new IllegalArgumentException("Invalid location");
		}
		// Warehouse Creation Feasibility
		long count = warehouseRepository.getAll().stream()
				.filter(w -> w.location.equals(data.getLocation()) && w.archivedAt == null).count();
		if (count >= location.maxNumberOfWarehouses) {
			throw new IllegalArgumentException("Maximum number of warehouses reached for this location");
		}
		// Capacity and Stock Validation
		int totalCapacity = warehouseRepository.getAll().stream()
				.filter(w -> w.location.equals(data.getLocation()) && w.archivedAt == null)
				.mapToInt(w -> w.capacity != null ? w.capacity : 0).sum();
		if (data.getCapacity() + totalCapacity > location.maxCapacity) {
			throw new IllegalArgumentException("Warehouse capacity exceeds location maximum");
		}
		if (data.getStock() > data.getCapacity()) {
			throw new IllegalArgumentException("Stock exceeds warehouse capacity");
		}
		// Create warehouse
		com.fulfilment.application.monolith.warehouses.domain.models.Warehouse warehouse = new com.fulfilment.application.monolith.warehouses.domain.models.Warehouse();
		warehouse.businessUnitCode = data.getBusinessUnitCode();
		warehouse.location = data.getLocation();
		warehouse.capacity = data.getCapacity();
		warehouse.stock = data.getStock();
		warehouse.createdAt = java.time.LocalDateTime.now();
		warehouseRepository.create(warehouse);
		return toWarehouseResponse(warehouse);
	}

	@Override
	public Warehouse getAWarehouseUnitByID(String id) {
		com.fulfilment.application.monolith.warehouses.domain.models.Warehouse warehouse = warehouseRepository
				.findByBusinessUnitCode(id);
		if (warehouse == null || warehouse.archivedAt != null) {
			throw new IllegalArgumentException("Warehouse not found or archived");
		}
		return toWarehouseResponse(warehouse);
	}

	@Override
	public void archiveAWarehouseUnitByID(String id) {
		com.fulfilment.application.monolith.warehouses.domain.models.Warehouse warehouse = warehouseRepository
				.findByBusinessUnitCode(id);
		if (warehouse == null || warehouse.archivedAt != null) {
			throw new IllegalArgumentException("Warehouse not found or already archived");
		}
		warehouse.archivedAt = java.time.LocalDateTime.now();
		warehouseRepository.update(warehouse);
	}

	@Override
	public Warehouse replaceTheCurrentActiveWarehouse(String businessUnitCode, @NotNull Warehouse data) {
		// Find the warehouse to replace
		com.fulfilment.application.monolith.warehouses.domain.models.Warehouse oldWarehouse = warehouseRepository
				.findByBusinessUnitCode(businessUnitCode);
		if (oldWarehouse == null || oldWarehouse.archivedAt != null) {
			throw new IllegalArgumentException("Warehouse to replace not found or already archived");
		}
		// Location Validation
		Location location = locationGateway.resolveByIdentifier(data.getLocation());
		if (location == null) {
			throw new IllegalArgumentException("Invalid location");
		}
		// Capacity Accommodation
		if (data.getCapacity() < oldWarehouse.stock) {
			throw new IllegalArgumentException("New warehouse capacity cannot accommodate old stock");
		}
		// Stock Matching
		if (!data.getStock().equals(oldWarehouse.stock)) {
			throw new IllegalArgumentException("Stock of new warehouse must match old warehouse");
		}
		// Archive old warehouse
		oldWarehouse.archivedAt = java.time.LocalDateTime.now();
		warehouseRepository.update(oldWarehouse);
		// Create new warehouse with same business unit code
		com.fulfilment.application.monolith.warehouses.domain.models.Warehouse newWarehouse = new com.fulfilment.application.monolith.warehouses.domain.models.Warehouse();
		newWarehouse.businessUnitCode = businessUnitCode;
		newWarehouse.location = data.getLocation();
		newWarehouse.capacity = data.getCapacity();
		newWarehouse.stock = data.getStock();
		newWarehouse.createdAt = java.time.LocalDateTime.now();
		warehouseRepository.create(newWarehouse);
		return toWarehouseResponse(newWarehouse);
	}

	private Warehouse toWarehouseResponse(
			com.fulfilment.application.monolith.warehouses.domain.models.Warehouse warehouse) {
		var response = new Warehouse();
		response.setBusinessUnitCode(warehouse.businessUnitCode);
		response.setLocation(warehouse.location);
		response.setCapacity(warehouse.capacity);
		response.setStock(warehouse.stock);

		return response;
	}
}
