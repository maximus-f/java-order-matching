package org.example.core;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

// Class to generate orders
public class OrderGenerator {

    /**
     *   Used to simulate incoming orders from server.
     *   Total volume is scaled to BTC. i.e. quantity scales to price of BTC.
     */
    public Order generateRandomOrder() {
        // only using BTC so matches appear quicker
        Map<String, Double> assetPrices = Map.of("BTC-USD", 50000.0);
        String instrument = new ArrayList<>(assetPrices.keySet()).get(new Random().nextInt(assetPrices.size()));
        double price = assetPrices.get(instrument);
        // scale quantity to correspond to BTC price
        double quantity = Math.round((Math.random() * Math.max(1, 50000 / price)) * 100.0) / 100.0;
        Order.Side side = new Random().nextBoolean() ? Order.Side.BUY : Order.Side.SELL;
        return new Order(instrument, price, quantity, side);
    }




}
