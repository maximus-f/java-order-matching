package org.example.interfaces;

import org.example.core.Order;

public interface EventListener {
    // This method will be called by the EventManager when a new order is received
    void onOrderUpdate(Order order);
}
