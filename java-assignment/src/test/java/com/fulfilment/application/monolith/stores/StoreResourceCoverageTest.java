package com.fulfilment.application.monolith.stores;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.WebApplicationException;

@QuarkusTest
public class StoreResourceCoverageTest {

	@Test
	public void testGetSingleReturnsStore() {
		StoreResource resource = new StoreResource();
		Store store = resource.getSingle(1L);
		assertNotNull(store);
		assertEquals(1L, store.id);
	}

	@Test
	public void testGetSingleThrowsForUnknown() {
		StoreResource resource = new StoreResource();
		Exception exception = assertThrows(WebApplicationException.class, () -> {
			resource.getSingle(999L);
		});
		assertTrue(exception.getMessage().contains("does not exist"));
	}

}
