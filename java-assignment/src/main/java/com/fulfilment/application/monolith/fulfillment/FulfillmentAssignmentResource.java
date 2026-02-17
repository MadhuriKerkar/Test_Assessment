package com.fulfilment.application.monolith.fulfillment;

import jakarta.ws.rs.*;
import jakarta.inject.Inject;
import java.util.List;

@Path("/fulfillment")
@Produces("application/json")
@Consumes("application/json")
public class FulfillmentAssignmentResource {
    @Inject
    FulfillmentAssignmentService service;

    @POST
    @Path("/assign")
    public void assignWarehouseToProductAndStore(@QueryParam("productId") Long productId,
                                                 @QueryParam("storeId") Long storeId,
                                                 @QueryParam("warehouseId") Long warehouseId) {
        service.assignWarehouseToProductAndStore(productId, storeId, warehouseId);
    }

    @GET
    @Path("/product-store")
    public List<FulfillmentAssignment> getAssignmentsForProductAndStore(@QueryParam("productId") Long productId,
                                                                       @QueryParam("storeId") Long storeId) {
        return service.getAssignmentsForProductAndStore(productId, storeId);
    }

    @GET
    @Path("/store/{storeId}")
    public List<FulfillmentAssignment> getAssignmentsForStore(@PathParam("storeId") Long storeId) {
        return service.getAssignmentsForStore(storeId);
    }

    @GET
    @Path("/warehouse/{warehouseId}")
    public List<FulfillmentAssignment> getAssignmentsForWarehouse(@PathParam("warehouseId") Long warehouseId) {
        return service.getAssignmentsForWarehouse(warehouseId);
    }
}
