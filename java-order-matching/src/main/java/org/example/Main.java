package org.example;

import org.example.core.EventManager;
import org.example.core.OrderListener;
import org.example.core.OrderMatcher;
import org.example.core.OrderWebSocketClient;

import java.util.concurrent.CountDownLatch;

public class Main {

    public static boolean PARTIAL;

    public static void main(String[] args) {
        PARTIAL = false;
        if (args.length > 0 && args[0].equalsIgnoreCase("-partial")) {
            PARTIAL = true;
            System.out.println("Running in partial matching mode.");
        } else {
            System.out.println("Running in complete matching mode.");
        }

        EventManager eventManager = new EventManager();
        OrderMatcher orderMatcher = new OrderMatcher(PARTIAL);
        OrderListener orderListener = new OrderListener(orderMatcher);
        eventManager.registerListener(orderListener);

        OrderWebSocketClient webSocketClient = new OrderWebSocketClient(eventManager);
        webSocketClient.connect("wss://echo.websocket.org");

        CountDownLatch latch = new CountDownLatch(1);
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}