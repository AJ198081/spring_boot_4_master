package dev.aj.order_service.model.order;

import dev.aj.order_service.model.common.NonNegativeAmount;
import dev.aj.order_service.model.shipping.Shipment;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record OrderResponse(UUID orderId, OrderStatus status, InvoiceStatus invoiceStatus, List<Shipment> shipment, List<Product> products) {

    public OrderResponse(UUID orderId, InvoiceStatus invoiceStatus, List<Shipment> shipments, List<Product> products) {
        this(orderId, OrderStatus.PENDING, invoiceStatus, shipments, products);
    }

    public record Product(UUID productId, NonNegativeAmount unitPrice, int quantity, NonNegativeAmount totalPrice) {
    }

    public sealed interface InvoiceStatus {
        record Due(UUID invoiceId, UUID orderId, UUID customerId, NonNegativeAmount amount, LocalDate dueDate) implements InvoiceStatus{
        }
        record Paid(UUID invoiceId, UUID orderId, UUID customerId, NonNegativeAmount amount) implements InvoiceStatus {
        }
    }

}

enum OrderStatus {
    PENDING,
    SHIPPED,
    COMPLETED,
    CANCELLED,
}
