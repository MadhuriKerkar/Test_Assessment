package com.fulfilment.application.monolith.products;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import java.math.BigDecimal;

@QuarkusTest
public class ProductEndpointTest {

  @Inject
  ProductResource resource;

  @Inject
  ProductRepository repository;

  @Test
  public void testProductConstructorWithName() {
    Product product = new Product("TestProduct");
    Assertions.assertEquals("TestProduct", product.name);
  }

  @Test
  @Transactional
  public void testCreateProductSuccess() {
    // Prepare a unique product
    Product product = new Product();
    product.name = "BILLY"; // unique, not in import.sql
    product.description = "Bookshelf";
    product.price = new BigDecimal("49.99");
    product.stock = 12;

    var response = resource.create(product);
    Assertions.assertEquals(201, response.getStatus());

    Product created = (Product) response.getEntity();
    Assertions.assertNotNull(created);
    Assertions.assertNotNull(created.id);
    Assertions.assertEquals("BILLY", created.name);

    // Verify persisted state
    Product found = repository.findById(created.id);
    Assertions.assertNotNull(found);
    Assertions.assertEquals("BILLY", found.name);
    Assertions.assertEquals("Bookshelf", found.description);
    Assertions.assertEquals(new BigDecimal("49.99"), found.price);
    Assertions.assertEquals(12, found.stock);
  }

  @Test
  @Transactional
  public void testUpdateProductSuccess() {
    // First, create a product to update
    Product toCreate = new Product();
    toCreate.name = "LACK";
    toCreate.description = "Table";
    toCreate.price = new BigDecimal("19.99");
    toCreate.stock = 5;
    var createResp = resource.create(toCreate);
    Product created = (Product) createResp.getEntity();

    // Prepare update payload
    Product updatePayload = new Product();
    updatePayload.name = "LACK-UPDATED";
    updatePayload.description = "Table (updated)";
    updatePayload.price = new BigDecimal("24.99");
    updatePayload.stock = 7;

    Product updated = resource.update(created.id, updatePayload);
    Assertions.assertNotNull(updated);
    Assertions.assertEquals(created.id, updated.id);
    Assertions.assertEquals("LACK-UPDATED", updated.name);
    Assertions.assertEquals("Table (updated)", updated.description);
    Assertions.assertEquals(new BigDecimal("24.99"), updated.price);
    Assertions.assertEquals(7, updated.stock);

    // Verify repository reflects changes
    Product found = repository.findById(created.id);
    Assertions.assertNotNull(found);
    Assertions.assertEquals("LACK-UPDATED", found.name);
    Assertions.assertEquals("Table (updated)", found.description);
    Assertions.assertEquals(new BigDecimal("24.99"), found.price);
    Assertions.assertEquals(7, found.stock);
  }
}
