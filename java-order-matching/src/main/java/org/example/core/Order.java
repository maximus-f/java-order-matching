package org.example.core;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Order {
    private final String instrument;
    private final double price;
    private double quantity;
    private final Side side;

    public enum Side {
        BUY, SELL
    }


    @JsonCreator
    public Order(
            @JsonProperty("instrument") String instrument,
            @JsonProperty("price") double price,
            @JsonProperty("quantity") double quantity,
            @JsonProperty("side") Side side) {
        this.instrument = instrument;
        this.price = price;
        this.quantity = quantity;
        this.side = side;
    }

    public String getInstrument() {
        return instrument;
    }

    /**
     * Reset quantity for partial match
     * @param quantity
     */
    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public double getQuantity() {
        return quantity;
    }

    public Side getSide() {
        return side;
    }

    @Override
    public String toString() {
        return "Order{" +
                "instrument='" + instrument + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", side=" + side +
                '}';
    }

    public String toFancyString() {
        return "Order: " + instrument + "(" + String.format("%.2f",quantity) + ", " + side + ")";
    }
}

