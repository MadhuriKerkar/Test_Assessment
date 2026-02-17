package com.fulfilment.application.monolith.fulfillment;

import com.fulfilment.application.monolith.products.Product;
import com.fulfilment.application.monolith.stores.Store;
import com.fulfilment.application.monolith.warehouses.adapters.database.DbWarehouse;
import jakarta.persistence.*;

@Entity
@Table(name = "fulfillment_assignment")
public class FulfillmentAssignment {
    @Id
    @GeneratedValue
    public Long id;

    @ManyToOne
    public Product product;

    @ManyToOne
    public Store store;

    @ManyToOne
    public DbWarehouse warehouse;
}
