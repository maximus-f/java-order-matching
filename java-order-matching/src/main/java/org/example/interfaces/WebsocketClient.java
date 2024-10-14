package org.example.interfaces;

public interface WebsocketClient {

    // Connect to a WebSocket server with a given URL
    void connect(String url);

    // Disconnect from the WebSocket server
    void disconnect();

    // Send a message to the WebSocket server
    void sendMessage(String message);

    // Handle incoming messages (to be implemented by the user)
    void onMessage(String message);

}
