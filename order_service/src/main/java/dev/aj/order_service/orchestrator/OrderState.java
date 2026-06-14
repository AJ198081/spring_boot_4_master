package dev.aj.order_service.orchestrator;

import dev.aj.order_service.model.common.PriceSummary;
import dev.aj.order_service.model.invoice.Invoice;
import dev.aj.order_service.model.order.Order;
import dev.aj.order_service.model.order.OrderRequest;
import dev.aj.order_service.model.shipping.Shipment;
import dev.aj.order_service.model.shipping.ShipmentResponse;

import java.util.List;

public sealed interface OrderState {

    record Placed(OrderRequest orderRequest) implements OrderState {
    }

    record OrderCancellationRequested(Order order) implements OrderState {}

    record Cancelled(Order order) implements OrderState {
    }

    record Validated(Order order) implements OrderState {
    }

    record Priced(Order order, PriceSummary priceSummary) implements OrderState {}

    record Invoiced(Order order, Invoice invoice) implements OrderState {
    }

    record Shipped(Order order, Invoice invoice, ShipmentResponse shipmentResponse) implements OrderState {
    }

    record Completed(Order order, Invoice invoice, List<Shipment> shipments) implements OrderState {
    }

    record Failed(Order order, Invoice invoice) implements OrderState {
    }
}
