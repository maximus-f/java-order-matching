# Order Matching Service - Java Task

## Overview

This task involves implementing an order matching service using WebSockets and event-driven architecture.
You will write a Java service that listens to a WebSocket for incoming order messages, processes those messages,
and logs whenever two matching orders are found. The client has been created, but you will need to trigger the 
websocket message events randomly simulating orders being received.

You are provided with the boilerplate for the `Order`, `EventManager`, `OrderMatcher`, and `OrderListener` classes,
along with interfaces for `EventListener`, `IOrderMatcher` and `WebSocketClient`.
You will implement the WebSocket client and the order processing logic.

### Task Breakdown

1. **Create a WebSocket Client**: Implement the `OrderWebsocketClient` file that connects to a dummy WebSocket server. Pick a WebSocket library of your choice.
2. **Trigger Orders**: Randomly generate orders from the WebSocket client (onMessage method).
3. **Order Processing**: Orders will contain fields like `instrument`, `price`, `quantity`, and `side`. You will need to implement a caching mechanism to store orders.
4. **Order matching**: When two orders match (same instrument, same price, qty match, but opposite sides), the goal is to LOG the matches.
5. **Event-Driven Architecture**: The WebSocket client should notify an event manager when new orders are received. The event manager will distribute these updates to registered listeners, such as the `OrderListener`.

---

## Project Structure

```bash
/src
└── main
    └── java
        └── org
            └── example
                └── core
                    ├── EventManager.java
                    ├── Order.java
                    ├── OrderListener.java
                    ├── OrderMatcher.java
                    ├── OrderWebSocketClient.java
                └── interfaces   
                    ├── EventListener.java
                    ├── IOrderMatcher.java
                    ├── WebSocketClient.java
                └── Main.java
└── test
    └── java
        └── org
            └── example
                └── core
                    ├── OrderMatchingTest.java
