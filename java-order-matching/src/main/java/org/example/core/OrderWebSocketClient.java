package org.example.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Main;
import org.example.interfaces.WebsocketClient;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class OrderWebSocketClient implements WebsocketClient {

    private final EventManager eventManager;
    private WebSocketClient webSocketClient;
    private final OrderGenerator orderGenerator;
    private final ScheduledExecutorService scheduler; // Scheduler for testing only


    public OrderWebSocketClient(EventManager eventManager) {
        this.eventManager = eventManager;
        this.orderGenerator = new OrderGenerator();
        this.scheduler = Executors.newScheduledThreadPool(1);

    }

    @Override
    public void connect(String url) {
        try {
            URI uri = new URI(url);
            webSocketClient = new WebSocketClient(uri) {
                @Override
                public void onOpen(ServerHandshake handshake) {
                    System.out.println("Connected to WebSocket server at " + url);

                    // set timings based on matching pattern used to show matching at appropriate speed.
                    // i.e. slower for partial matching since matches occur more frequently
                    TimeUnit timeUnit = TimeUnit.SECONDS;
                    int period = 1;
                    if (!Main.PARTIAL) {
                        timeUnit = TimeUnit.MILLISECONDS;
                        period = 250;
                    }
                    scheduler.scheduleAtFixedRate(() -> {
                        Order simulatedOrder = orderGenerator.generateRandomOrder();
                        ObjectMapper objectMapper = new ObjectMapper();
                        try {
                            String orderJson = objectMapper.writeValueAsString(simulatedOrder);
                            webSocketClient.send(orderJson);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }, 0, period, timeUnit);

                    scheduler.schedule(() -> {
                        try {
                            System.out.println("Shutting down scheduler...");
                            scheduler.shutdown();
                            // wait 2 seconds for tasks scheduled during shutdown to finish
                            if (!scheduler.awaitTermination(2, TimeUnit.SECONDS)) {
                                scheduler.shutdownNow();
                            }
                            eventManager.printStatistics();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }, 10, TimeUnit.SECONDS);

                }

                @Override
                public void onMessage(String message) {

                    if (isValidJson(message)) {
                        Order order = parseOrderFromMessage(message);
                        if (order != null) {
                            eventManager.notifyListeners(order);
                        } else {
                            System.out.println("Received invalid order: " + message);
                        }
                    } else {
                        System.out.println("Non-JSON message received: " + message);
                    }
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    System.out.println("Disconnected from WebSocket server. Reason: " + reason);
                }

                @Override
                public void onError(Exception ex) {
                    System.out.println("Error occurred: " + ex.getMessage());
                }
            };
            webSocketClient.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private boolean isValidJson(String message) {
        return message != null && (message.startsWith("{") && message.endsWith("}"));
    }

    private Order parseOrderFromMessage(String message) {
       ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(message, Order.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    // Anonymous inner-class used instead of default methods
    @Override
    public void disconnect() {

    }

    @Override
    public void sendMessage(String message) {

    }


    @Override
    public void onMessage(String message) {

    }





}

