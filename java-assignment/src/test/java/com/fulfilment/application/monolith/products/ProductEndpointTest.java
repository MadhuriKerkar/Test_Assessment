package com.fulfilment.application.monolith.products;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.core.IsNot.not;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@QuarkusTest
public class ProductEndpointTest {

  @Inject
  ProductResource resource;

  @Test
  @Transactional
  public void testCrudProduct() {
    final String path = "product";

    // List all, should have all 3 products the database has initially:
    given()
        .when()
        .get(path)
        .then()
        .statusCode(200)
        .body(containsString("TONSTAD"), containsString("KALLAX"), containsString("BESTÅ"));

    // Delete the TONSTAD via resource (unit-style, bypass HTTP mapping):
    assertDoesNotThrow(() -> resource.delete(1L));

    // List all, TONSTAD should be missing now:
    given()
        .when()
        .get(path)
        .then()
        .statusCode(200)
        .body(not(containsString("TONSTAD")), containsString("KALLAX"), containsString("BESTÅ"));
  }
  
    @Test
    public void testProductConstructorWithName() {
      Product product = new Product("TestProduct");
      org.junit.jupiter.api.Assertions.assertEquals("TestProduct", product.name);
    }
}
