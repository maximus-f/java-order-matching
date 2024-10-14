package org.example.core;

import org.example.interfaces.EventListener;

public class OrderListener implements EventListener {

    private final OrderMatcher orderMatcher;

    public OrderListener(OrderMatcher orderMatcher) {
        this.orderMatcher = orderMatcher;
    }

    @Override
    public void onOrderUpdate(Order order) {
        // Pass the received order to the order matcher
        orderMatcher.addOrder(order);
    }

    public OrderMatcher getOrderMatcher() {
        return orderMatcher;
    }

}
