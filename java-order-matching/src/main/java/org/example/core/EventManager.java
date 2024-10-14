package org.example.core;


import org.example.interfaces.EventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EventManager {
    // Listeners and event handling logic and thread management
    private final List<EventListener> listeners = new ArrayList<>();


    public void registerListener(EventListener listener) {
        // Add the listener to a list
        listeners.add(listener);
    }

    public void notifyListeners(Order order) {
        // Notify all listeners about a new order
       listeners.forEach(eventListener -> eventListener.onOrderUpdate(order));
    }


    // Used to print statistics at end of session
    public void printStatistics() {
        if (!listeners.isEmpty()) {
            EventListener listener = listeners.get(0);
            // Casting here to show statistics
            if (listener instanceof OrderListener orderListener) {
                Map<String, List<Order>> book = orderListener.getOrderMatcher().getOrderBook();
                orderListener.getOrderMatcher().getStatistics().printStatistics(book);
            }
        }
    }
}

