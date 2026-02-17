package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import jakarta.inject.Inject;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class ArchiveWarehouseUseCaseTest {

	@Inject
	WarehouseStore warehouseStore;

	@Inject
	ArchiveWarehouseUseCase useCase;

	@Test
	@Transactional
	public void testArchiveCallsUpdate() {
		Warehouse warehouse = new Warehouse();
		useCase.archive(warehouse);
	}
}
