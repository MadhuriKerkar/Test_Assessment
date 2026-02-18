package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import jakarta.inject.Inject;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class ArchiveWarehouseUseCaseTest {

	@Inject
	WarehouseStore warehouseStore;

	@Inject
	ArchiveWarehouseUseCase useCase;

	@Test
	@Transactional
	public void testArchiveRemovesWarehouse() {
		Warehouse existing = new Warehouse();
		existing.businessUnitCode = "ARCHIVE_TEST_BU";
		existing.location = "TEST_LOC";
		existing.capacity = 100;
		existing.stock = 10;
		warehouseStore.create(existing);

		Warehouse toArchive = new Warehouse();
		toArchive.businessUnitCode = "ARCHIVE_TEST_BU";
		useCase.archive(toArchive);

		Warehouse found = warehouseStore.findByBusinessUnitCode("ARCHIVE_TEST_BU");
		assertNull(found);
	}

	@Test
	@Transactional
	public void testArchiveThrowsWhenNotFound() {
		Warehouse missing = new Warehouse();
		missing.businessUnitCode = "MISSING_BU";
		assertThrows(IllegalArgumentException.class, () -> useCase.archive(missing));
	}
}
