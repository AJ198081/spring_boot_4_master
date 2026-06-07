package dev.aj.data_oriented_programming.practice.sealed_interfaces;

import java.time.Instant;

public sealed interface OrderStatus {

    record Pending(Details details, Instant createdAt) implements OrderStatus { }
    record Shipped(Details details, Instant shippedAt, Address shippingAddress, String shippingTrackingNumber, String courier) implements OrderStatus { }
    record Delivered(Details details, Instant deliveredAt, Address deliveryAddress) implements OrderStatus { }
    record Cancelled(Details details, Instant cancelledAt, String cancelReason) implements OrderStatus { }
    record Refunded(Details details, Instant refundedAt, String refundReason) implements  OrderStatus { }
}

record Details(String orderNumber, String productId, String customerId) {
}

record Address(String address) {}