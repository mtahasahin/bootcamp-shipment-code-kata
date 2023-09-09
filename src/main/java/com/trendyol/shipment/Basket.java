package com.trendyol.shipment;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

public class Basket {
    private static final int BASKET_SIZE_THRESHOLD = 3;
    private static final List<ShipmentSize> shipmentSizeOrder = asList(ShipmentSize.SMALL, ShipmentSize.MEDIUM, ShipmentSize.LARGE, ShipmentSize.X_LARGE);
    private List<Product> products;

    public ShipmentSize getShipmentSize() {
        Map<ShipmentSize, Long> shipmentSizeCounts = getShipmentSizeCounts();

        for (var entry : shipmentSizeCounts.entrySet()) {
            ShipmentSize size = entry.getKey();
            Long count = entry.getValue();
            if (count >= BASKET_SIZE_THRESHOLD) {
                return getNextLargerShipmentSize(size);
            }
        }

        return getLargestSizeThatExistsInBasket(shipmentSizeCounts);
    }

    private Map<ShipmentSize, Long> getShipmentSizeCounts() {
        return products
                .stream()
                .collect(Collectors.groupingBy(Product::getSize, Collectors.counting()));
    }

    private ShipmentSize getNextLargerShipmentSize(ShipmentSize size) {
        int index = shipmentSizeOrder.indexOf(size);
        return index < shipmentSizeOrder.size() - 1 ?
                shipmentSizeOrder.get(index + 1) :
                shipmentSizeOrder.get(index);
    }

    private ShipmentSize getLargestSizeThatExistsInBasket(Map<ShipmentSize, Long> shipmentSizeCountMap) {
        return shipmentSizeCountMap
                .keySet()
                .stream()
                .max(Comparator.comparingInt(shipmentSizeOrder::indexOf))
                .orElseThrow(EmptyBasketException::new);
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
