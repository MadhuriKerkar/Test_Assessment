package com.fulfilment.application.monolith.warehouses.adapters.restapi;

import com.fulfilment.application.monolith.warehouses.adapters.database.WarehouseRepository;
import com.fulfilment.application.monolith.warehouses.domain.usecases.ArchiveWarehouseUseCase;
import com.fulfilment.application.monolith.warehouses.domain.usecases.CreateWarehouseUseCase;
import com.fulfilment.application.monolith.warehouses.domain.usecases.ReplaceWarehouseUseCase;
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
	private CreateWarehouseUseCase createWarehouseUseCase;

	@Inject
	private ReplaceWarehouseUseCase replaceWarehouseUseCase;

	@Inject
	private ArchiveWarehouseUseCase archiveWarehouseUseCase;

	@Override
	public List<Warehouse> listAllWarehousesUnits() {
		return warehouseRepository.getAll().stream().map(this::toWarehouseResponse).toList();
	}

	@Override
	public Warehouse createANewWarehouseUnit(@NotNull Warehouse data) {
		com.fulfilment.application.monolith.warehouses.domain.models.Warehouse warehouse = new com.fulfilment.application.monolith.warehouses.domain.models.Warehouse();
		warehouse.businessUnitCode = data.getBusinessUnitCode();
		warehouse.location = data.getLocation();
		warehouse.capacity = data.getCapacity();
		warehouse.stock = data.getStock();

		createWarehouseUseCase.create(warehouse);

		// fetch the created warehouse to return
		com.fulfilment.application.monolith.warehouses.domain.models.Warehouse created = warehouseRepository
				.findByBusinessUnitCode(data.getBusinessUnitCode());
		return toWarehouseResponse(created);
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
		com.fulfilment.application.monolith.warehouses.domain.models.Warehouse warehouse = new com.fulfilment.application.monolith.warehouses.domain.models.Warehouse();
		warehouse.businessUnitCode = id;
		archiveWarehouseUseCase.archive(warehouse);
	}

	@Override
	public Warehouse replaceTheCurrentActiveWarehouse(String businessUnitCode, @NotNull Warehouse data) {
		com.fulfilment.application.monolith.warehouses.domain.models.Warehouse newWarehouse = new com.fulfilment.application.monolith.warehouses.domain.models.Warehouse();
		newWarehouse.businessUnitCode = businessUnitCode;
		newWarehouse.location = data.getLocation();
		newWarehouse.capacity = data.getCapacity();
		newWarehouse.stock = data.getStock();

		replaceWarehouseUseCase.replace(newWarehouse);

		com.fulfilment.application.monolith.warehouses.domain.models.Warehouse replaced = warehouseRepository
				.findByBusinessUnitCode(businessUnitCode);
		return toWarehouseResponse(replaced);
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
