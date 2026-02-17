package com.fulfilment.application.monolith.fulfillment;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

@QuarkusTest
public class FulfillmentAssignmentResourceTest {

	@Inject
	FulfillmentAssignmentResource resource;

	@Test
	@Transactional
	public void testGetAssignmentsForProductAndStoreReturnsList() {
		List<FulfillmentAssignment> assignments = resource.getAssignmentsForProductAndStore(1L, 1L);
		assertNotNull(assignments);
	}

	@Test
	@Transactional
	public void testGetAssignmentsForStoreReturnsList() {
		List<FulfillmentAssignment> assignments = resource.getAssignmentsForStore(1L);
		assertNotNull(assignments);
	}

	@Test
	@Transactional
	public void testGetAssignmentsForWarehouseReturnsList() {
		List<FulfillmentAssignment> assignments = resource.getAssignmentsForWarehouse(1L);
		assertNotNull(assignments);
	}
}
