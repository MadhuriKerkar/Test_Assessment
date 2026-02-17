package com.fulfilment.application.monolith.common;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.metadata.ConstraintDescriptor;
import jakarta.ws.rs.core.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Set;
import java.util.Collections;
import java.util.Iterator;

@QuarkusTest
public class ConstraintViolationExceptionMapperTest {

	@Test
	public void testToResponseWithViolations() {
		ObjectMapper objectMapper = new ObjectMapper();
		ConstraintViolationExceptionMapper mapper = new ConstraintViolationExceptionMapper();
		mapper.objectMapper = objectMapper;

		ConstraintViolation<?> violation = new DummyConstraintViolation("field", "must not be null");
		Set<ConstraintViolation<?>> violations = Collections.singleton(violation);
		ConstraintViolationException exception = new ConstraintViolationException(violations);

		Response response = mapper.toResponse(exception);
		assertEquals(400, response.getStatus());
		String json = response.getEntity().toString();
		assertTrue(json.contains("Validation failed"));
		assertTrue(json.contains("field: must not be null"));
	}

	static class DummyConstraintViolation implements ConstraintViolation<Object> {
		private final String propertyPath;
		private final String message;

		DummyConstraintViolation(String propertyPath, String message) {
			this.propertyPath = propertyPath;
			this.message = message;
		}

		@Override
		public String getMessage() {
			return message;
		}

		@Override
		public String getMessageTemplate() {
			return null;
		}

		@Override
		public Object getRootBean() {
			return null;
		}

		@Override
		public Class<Object> getRootBeanClass() {
			return null;
		}

		@Override
		public Object getLeafBean() {
			return null;
		}

		@Override
		public Object[] getExecutableParameters() {
			return new Object[0];
		}

		@Override
		public Object getExecutableReturnValue() {
			return null;
		}

		@Override
		public jakarta.validation.Path getPropertyPath() {
			return new DummyPath(propertyPath);
		}

		@Override
		public Object getInvalidValue() {
			return null;
		}

		@Override
		public ConstraintDescriptor<?> getConstraintDescriptor() {
			return null;
		}

		@Override
		public <U> U unwrap(Class<U> type) {
			return null;
		}
	}

	static class DummyPath implements jakarta.validation.Path {
		private final String path;

		DummyPath(String path) {
			this.path = path;
		}

		@Override
		public String toString() {
			return path;
		}

		@Override
		public Iterator<Node> iterator() {
			return Collections.emptyIterator();
		}
	}
}
