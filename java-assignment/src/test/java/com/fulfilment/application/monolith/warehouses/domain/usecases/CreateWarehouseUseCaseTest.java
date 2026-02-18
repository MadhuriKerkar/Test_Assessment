
package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import jakarta.inject.Inject;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class CreateWarehouseUseCaseTest {

	@Inject
	WarehouseStore warehouseStore;

	@Inject
	CreateWarehouseUseCase useCase;

	@Test
	@Transactional
	public void testCreateThrowsForInvalidLocation() {
		Warehouse warehouse = new Warehouse();
		warehouse.businessUnitCode = "CREATE_INVALID_LOC_BU";
		warehouse.location = "INVALID_LOC"; 
		warehouse.capacity = 10;
		warehouse.stock = 5;
		assertThrows(IllegalArgumentException.class, () -> useCase.create(warehouse));
	}

	@Test
	@Transactional
	public void testCreateThrowsForDuplicateBusinessUnitCode() {
		Warehouse existing = new Warehouse();
		existing.businessUnitCode = "DUPLICATE_BU";
		existing.location = "AMSTERDAM-001";
		existing.capacity = 10;
		existing.stock = 5;
		warehouseStore.create(existing);

		Warehouse warehouse = new Warehouse();
		warehouse.businessUnitCode = "DUPLICATE_BU"; // same BU
		warehouse.location = "AMSTERDAM-001";
		warehouse.capacity = 10;
		warehouse.stock = 5;
		assertThrows(IllegalArgumentException.class, () -> useCase.create(warehouse));
	}

	@Test
	@Transactional
	public void testCreateThrowsWhenStockExceedsCapacity() {
		Warehouse warehouse = new Warehouse();
		warehouse.businessUnitCode = "STOCK_GT_CAP_BU";
		warehouse.location = "AMSTERDAM-001";
		warehouse.capacity = 10;
		warehouse.stock = 11; // exceeds capacity
		assertThrows(IllegalArgumentException.class, () -> useCase.create(warehouse));
	}

	@Test
	@Transactional
	public void testCreateSucceeds() {
		Warehouse warehouse = new Warehouse();
		warehouse.businessUnitCode = "CREATE_OK_BU";
		warehouse.location = "HELMOND-001";
		warehouse.capacity = 20;
		warehouse.stock = 10;

		assertDoesNotThrow(() -> useCase.create(warehouse));

		Warehouse found = warehouseStore.findByBusinessUnitCode("CREATE_OK_BU");
		assertNotNull(found);
		assertEquals("HELMOND-001", found.location);
		assertEquals(20, found.capacity);
		assertEquals(10, found.stock);
		assertNotNull(found.createdAt);
	}
}
