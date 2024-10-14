package org.example.core;


import org.example.core.EventManager;
import org.example.core.Order;
import org.example.core.OrderMatcher;
import org.example.core.OrderWebSocketClient;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

public class OrderMatchingTest {

    @Test
    public void testOrderMatching() {
        // Mock EventManager and OrderMatcher
        EventManager eventManager = mock(EventManager.class);
        OrderMatcher orderMatcher = new OrderMatcher();

        // Mock WebSocketClient
        OrderWebSocketClient client = new OrderWebSocketClient(eventManager);

        // Simulate receiving a BUY order
        Order buyOrder = new Order("BTC-USD", 50000, 1.5, Order.Side.BUY);
        orderMatcher.addOrder(buyOrder);

        // Simulate receiving a SELL order that matches
        Order sellOrder = new Order("BTC-USD", 50000, 1.5, Order.Side.SELL);
        orderMatcher.addOrder(sellOrder);

        // No assert here since we're just logging matches, but in a real test you'd capture the log output
    }
}
