
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
	public void testCreateThrowsWhenLocationAtMaxWarehouses() {
		// HELMOND-001 allows max 1 active warehouse
		Warehouse occupant = new Warehouse();
		occupant.businessUnitCode = "HELMOND_MAX_OCCUPANT_BU";
		occupant.location = "HELMOND-001";
		occupant.capacity = 20;
		occupant.stock = 5;
		warehouseStore.create(occupant);

		Warehouse another = new Warehouse();
		another.businessUnitCode = "HELMOND_MAX_OVERFLOW_BU";
		another.location = "HELMOND-001";
		another.capacity = 10;
		another.stock = 5;

		assertThrows(IllegalArgumentException.class, () -> useCase.create(another));
	}

	@Test
	@Transactional
	public void testCreateThrowsWhenAggregateCapacityExceedsLocationMax() {
		// AMSTERDAM-002: maxNumberOfWarehouses=3, maxCapacity=75
		Warehouse w1 = new Warehouse();
		w1.businessUnitCode = "AMS2_CAP_W1_BU";
		w1.location = "AMSTERDAM-002";
		w1.capacity = 40;
		w1.stock = 10;
		warehouseStore.create(w1);

		Warehouse w2 = new Warehouse();
		w2.businessUnitCode = "AMS2_CAP_W2_BU";
		w2.location = "AMSTERDAM-002";
		w2.capacity = 30;
		w2.stock = 10;
		warehouseStore.create(w2);

		// Attempt to create third pushes totalCapacity to 76 (>75)
		Warehouse w3 = new Warehouse();
		w3.businessUnitCode = "AMS2_CAP_W3_BU";
		w3.location = "AMSTERDAM-002";
		w3.capacity = 6;
		w3.stock = 5;

		assertThrows(IllegalArgumentException.class, () -> useCase.create(w3));
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
