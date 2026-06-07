package dev.aj.data_oriented_programming.practice.sealed_interfaces;

public record Order(String orderNumber, String productId, String customerId, boolean isPaid, boolean isShipped, boolean isDelivered, boolean isCancelled, boolean isRefunded, String cancelReason, String refundReason) {
}

