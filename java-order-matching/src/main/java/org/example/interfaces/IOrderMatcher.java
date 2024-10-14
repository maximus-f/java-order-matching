package org.example.interfaces;

import org.example.core.Order;

public interface IOrderMatcher {

    // Add a new order to the order book
    void addOrder(Order order);

    boolean isMatch(Order o1, Order o2);

}
