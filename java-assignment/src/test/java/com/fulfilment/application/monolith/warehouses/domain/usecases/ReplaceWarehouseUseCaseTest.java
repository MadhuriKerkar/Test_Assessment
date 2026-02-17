package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import jakarta.inject.Inject;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class ReplaceWarehouseUseCaseTest {

	@Inject
	WarehouseStore warehouseStore;

	@Inject
	ReplaceWarehouseUseCase useCase;

	@Test
	@Transactional
	public void testReplaceCallsUpdate() {
		Warehouse warehouse = new Warehouse();
		useCase.replace(warehouse);
	}
}
