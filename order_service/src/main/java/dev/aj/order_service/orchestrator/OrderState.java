package dev.aj.order_service.orchestrator;

import dev.aj.order_service.model.common.PriceSummary;
import dev.aj.order_service.model.invoice.Invoice;
import dev.aj.order_service.model.order.Order;
import dev.aj.order_service.model.shipping.Shipment;
import dev.aj.order_service.model.shipping.ShipmentStatus;

import java.util.List;

public sealed interface OrderState {

    Order order();

    record Placed(Order order) implements OrderState {
    }

    record OrderCancellationRequested(Order order) implements OrderState {}

    record Cancelled(Order order) implements OrderState {
    }

    record Validated(Order order) implements OrderState {
    }

    record Priced(Order order, PriceSummary priceSummary) implements OrderState {}

    record Invoiced(Order order, Invoice invoice) implements OrderState {
    }

    record Shipped(Order order, Invoice invoice, ShipmentStatus.Scheduled shipmentResponse) implements OrderState {
    }

    record Completed(Order order, Invoice invoice, List<Shipment> shipments) implements OrderState {
    }

    record Failed(Order order, Invoice invoice) implements OrderState {
    }
}
