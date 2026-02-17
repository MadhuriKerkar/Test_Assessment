package com.fulfilment.application.monolith.location;

import com.fulfilment.application.monolith.warehouses.domain.models.Location;
import org.junit.jupiter.api.Test;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class LocationGatewayTest {
	@Inject
	LocationGateway locationGateway;

	@Test
	public void testWhenResolveExistingLocationShouldReturn() {
		Location location = locationGateway.resolveByIdentifier("ZWOLLE-001");
		assertNotNull(location);
		assertEquals("ZWOLLE-001", location.identification);
	}

	@Test
	public void testResolveByIdentifierReturnsNullForUnknown() {
		LocationGateway locationGateway = new LocationGateway();
		var location = locationGateway.resolveByIdentifier("UNKNOWN");
		assertNull(location);
	}
}
