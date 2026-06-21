package dev.aj.order_service.model.order;

import dev.aj.order_service.model.common.NonNegativeAmount;
import dev.aj.order_service.model.shipping.ShipmentItem;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@SuppressWarnings("unused")
public record OrderResponse(UUID orderId, OrderStatus status, InvoiceStatus invoiceStatus, BigDecimal amountCharged, List<ShipmentItem> shipmentItems) {

    public OrderResponse(UUID orderId, InvoiceStatus invoiceStatus, BigDecimal amountCharged, List<ShipmentItem> shipmentItems) {
        this(orderId, OrderStatus.PENDING, invoiceStatus, amountCharged, shipmentItems);
    }

    public record Product(UUID productId, NonNegativeAmount unitPrice, int quantity, NonNegativeAmount totalPrice) {
    }

    public enum InvoiceStatus {
        Due, Paid, CANCELLED
    }

    public enum OrderStatus {
        PENDING,
        SHIPPED,
        COMPLETED,
        CANCELLED,
    }

}
