package dev.aj.order_service.service;

import dev.aj.order_service.model.common.PriceSummary;
import dev.aj.order_service.model.invoice.Invoice;
import dev.aj.order_service.model.order.Order;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface PaymentService {

    Invoice processPayment(Order order, PriceSummary priceSummary);
}
