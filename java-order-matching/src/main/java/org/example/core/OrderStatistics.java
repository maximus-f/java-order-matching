package org.example.core;

import org.example.datatypes.AtomicFloat;

import java.text.NumberFormat;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Record orders, types, volume and unfilled orders at end of a session.
 */
public class OrderStatistics {


    // Atomic datatypes not necessary here but used to handle thread-safety if statistics were a global variable
    private final AtomicInteger totalOrders = new AtomicInteger(0);
    private final AtomicInteger buyOrders = new AtomicInteger(0);
    private final AtomicInteger sellOrders = new AtomicInteger(0);
    private final AtomicInteger totalMatches = new AtomicInteger(0);
    private final AtomicFloat totalTradedVolume = new AtomicFloat(0);

    public void recordOrder(Order order) {
        totalOrders.incrementAndGet();
        if (order.getSide() == Order.Side.BUY) {
            buyOrders.incrementAndGet();
        } else if (order.getSide() == Order.Side.SELL) {
            sellOrders.incrementAndGet();
        }
    }

    public void recordMatch(double quantity, double price){
        totalMatches.incrementAndGet();
        totalTradedVolume.getAndSet((float) (totalTradedVolume.get() + (quantity * price)));
    }

    public void printStatistics(Map<String, List<Order>> book) {
        System.out.println("==== Order Statistics ====");
        System.out.println("Total Orders: " + totalOrders.get());
        System.out.println("Total Matches: " + totalMatches.get());
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
         String formattedVolume = numberFormat.format(totalTradedVolume.get());
        System.out.println("Total Traded Volumes: $" + formattedVolume + " USD");
        System.out.println("Buy Orders: " + buyOrders.get());
        System.out.println("Sell Orders: " + sellOrders.get());
        System.out.println("==== Unfilled Orders ====");
        for (Map.Entry<String, List<Order>> entry : book.entrySet()) {
            List<Order> orders = entry.getValue();
            for (Order order : orders) {
                System.out.println(order.toFancyString());
            }
        }
    }
}
