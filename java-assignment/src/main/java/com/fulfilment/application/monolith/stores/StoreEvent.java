package com.fulfilment.application.monolith.stores;

public class StoreEvent {
    public enum StoreEventType { CREATE, UPDATE }
    public final Store store;
    public final StoreEventType type;

    public StoreEvent(Store store, StoreEventType type) {
        this.store = store;
        this.type = type;
    }
}