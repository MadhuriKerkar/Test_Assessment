package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import jakarta.inject.Inject;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class ReplaceWarehouseUseCaseTest {

	@Inject
	WarehouseStore warehouseStore;

	@Inject
	ReplaceWarehouseUseCase useCase;

	@Test
	@Transactional
	public void testReplaceThrowsWhenNotFound() {
		Warehouse newWarehouse = new Warehouse();
		newWarehouse.businessUnitCode = "REPLACE_MISSING_BU";
		newWarehouse.location = "AMSTERDAM-001";
		newWarehouse.capacity = 10;
		newWarehouse.stock = 5;
		assertThrows(IllegalArgumentException.class, () -> useCase.replace(newWarehouse));
	}

	@Test
	@Transactional
	public void testReplaceThrowsForInvalidLocation() {
		Warehouse old = new Warehouse();
		old.businessUnitCode = "REPLACE_INVALID_LOC_BU";
		old.location = "AMSTERDAM-001";
		old.capacity = 50;
		old.stock = 20;
		warehouseStore.create(old);

		Warehouse newWarehouse = new Warehouse();
		newWarehouse.businessUnitCode = "REPLACE_INVALID_LOC_BU";
		newWarehouse.location = "INVALID_LOC";
		newWarehouse.capacity = 50;
		newWarehouse.stock = 20;
		assertThrows(IllegalArgumentException.class, () -> useCase.replace(newWarehouse));
	}

	@Test
	@Transactional
	public void testReplaceThrowsForInsufficientCapacity() {
		Warehouse old = new Warehouse();
		old.businessUnitCode = "REPLACE_SMALL_CAP_BU";
		old.location = "AMSTERDAM-001";
		old.capacity = 50;
		old.stock = 20;
		warehouseStore.create(old);

		Warehouse newWarehouse = new Warehouse();
		newWarehouse.businessUnitCode = "REPLACE_SMALL_CAP_BU";
		newWarehouse.location = "AMSTERDAM-001";
		newWarehouse.capacity = 10;
		newWarehouse.stock = 20;
		assertThrows(IllegalArgumentException.class, () -> useCase.replace(newWarehouse));
	}

	@Test
	@Transactional
	public void testReplaceThrowsForStockMismatch() {
		Warehouse old = new Warehouse();
		old.businessUnitCode = "REPLACE_STOCK_MISMATCH_BU";
		old.location = "AMSTERDAM-001";
		old.capacity = 50;
		old.stock = 20;
		warehouseStore.create(old);

		Warehouse newWarehouse = new Warehouse();
		newWarehouse.businessUnitCode = "REPLACE_STOCK_MISMATCH_BU";
		newWarehouse.location = "AMSTERDAM-001";
		newWarehouse.capacity = 50;
		newWarehouse.stock = 19; // mismatch
		assertThrows(IllegalArgumentException.class, () -> useCase.replace(newWarehouse));
	}

	@Test
	@Transactional
	public void testReplaceSucceeds() {
		Warehouse old = new Warehouse();
		old.businessUnitCode = "REPLACE_OK_BU";
		old.location = "AMSTERDAM-001";
		old.capacity = 50;
		old.stock = 20;
		warehouseStore.create(old);

		Warehouse newWarehouse = new Warehouse();
		newWarehouse.businessUnitCode = "REPLACE_OK_BU";
		newWarehouse.location = "AMSTERDAM-002";
		newWarehouse.capacity = 60;
		newWarehouse.stock = 20;

		assertDoesNotThrow(() -> useCase.replace(newWarehouse));

		var withBu = warehouseStore.getAll().stream()
			.filter(w -> "REPLACE_OK_BU".equals(w.businessUnitCode))
			.toList();
		assertTrue(withBu.stream().anyMatch(w -> "AMSTERDAM-002".equals(w.location) && Integer.valueOf(60).equals(w.capacity) && Integer.valueOf(20).equals(w.stock)));
		assertTrue(withBu.stream().anyMatch(w -> w.archivedAt != null));
	}
}
