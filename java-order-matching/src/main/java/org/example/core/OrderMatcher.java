package org.example.core;

import org.example.interfaces.IOrderMatcher;

import java.util.*;

/**
 * This class is responsible for matching buy and sell orders for a given instrument.
 */
public class OrderMatcher implements IOrderMatcher {


    private final Map<String, List<Order>> orderBook = new HashMap<>();

    private final boolean partial;

    private final OrderStatistics statistics;

    public OrderMatcher(boolean partial) {
        this.statistics = new OrderStatistics();
        this.partial = partial;
    }

    public OrderMatcher() {
        this.partial = false;
        this.statistics = new OrderStatistics();
    }

    /**
     *  Check for potential matches, if found, log it is as a match.
     *  Otherwise, add it to the cache.
     * @param order
     */
    @Override
    public void addOrder(Order order) {
        statistics.recordOrder(order);
        orderBook.putIfAbsent(order.getInstrument(), new ArrayList<>());
        List<Order> ordersForInstrument = orderBook.get(order.getInstrument());
        // match with existing orders
        if (partial) System.out.println(" ");
        System.out.println("New: "  + order.toFancyString());
        if (partial)  ordersForInstrument.forEach(o -> System.out.println(o.toFancyString()));
        if (!ordersForInstrument.isEmpty()) {
            for (Order existingOrder : ordersForInstrument) {
                if (existingOrder.getInstrument().equals(order.getInstrument())) {
                    if (partial) {
                        // TODO better logging here potentially.
                        partialMatchOrders(order, ordersForInstrument);
                        return;
                    } else {
                        if (isMatch(order, existingOrder)) {
                            System.out.println("Found match for " + order.getInstrument() + " " + order.getQuantity() + " with " + existingOrder.getInstrument() + " " + existingOrder.getQuantity());
                            System.out.println(" ");
                            statistics.recordMatch( order.getQuantity(), order.getPrice());
                            ordersForInstrument.remove(existingOrder);
                            return;
                        }
                    }
                }
            }
        }
        // No match so add to list
        ordersForInstrument.add(order);
        System.out.println("Order added to book: " + order);
    }



    /**
     *  Matching orders if instrument is the same and buy and sell are equal and
     *  order sides are not the same.
     *
     * @param order1
     * @param order2
     * @return True if matched, false otherwise
     */
    @Override
    public boolean isMatch(Order order1, Order order2) {

        // price is hardcoded to keep things simple
        return order1.getInstrument().equals(order2.getInstrument()) &&
                order1.getQuantity() == order2.getQuantity() &&
                order1.getSide() != order2.getSide();  }


    // Same asset & opposite side
    private boolean isPartialMatchAvalible(Order existingOrder, Order incomingOrder) {
        return existingOrder.getInstrument().equals(incomingOrder.getInstrument()) &&
                existingOrder.getPrice() == incomingOrder.getPrice() &&
                !existingOrder.getSide().equals(incomingOrder.getSide());
    }

    // Match rolling partial orders or whole if possible
    private void partialMatchOrders(Order incomingOrder, List<Order> orderBook) {
        Iterator<Order> iterator = orderBook.iterator();

        while (iterator.hasNext()) {
            Order existingOrder = iterator.next();
            if (isPartialMatchAvalible(incomingOrder, existingOrder)) {
                double matchQuantity = Math.min(existingOrder.getQuantity(), incomingOrder.getQuantity());
                existingOrder.setQuantity(existingOrder.getQuantity() - matchQuantity);
                incomingOrder.setQuantity(incomingOrder.getQuantity() - matchQuantity);

                System.out.println("Matched " + String.format("%.2f", matchQuantity) + " units of " + incomingOrder.getInstrument());
                System.out.println("Incoming order now: " + incomingOrder.toFancyString());
                System.out.println("Existing order now: " + existingOrder.toFancyString());
                System.out.println(" ");

                statistics.recordMatch(matchQuantity, existingOrder.getPrice());
                if (existingOrder.getQuantity() == 0) {
                    iterator.remove();
                }

                if (incomingOrder.getQuantity() == 0) {
                    break;
                }
            }
        }
        if (incomingOrder.getQuantity() > 0) {
            // Add left over quantity to order book
            orderBook.add(incomingOrder);
        }
    }


    // below methods used for printing stats on run-time
    public OrderStatistics getStatistics() {
        return statistics;
    }

    public Map<String, List<Order>> getOrderBook() {
        return orderBook;
    }
}
