package dev.aj.order_service.orchestrator.impl;

import dev.aj.order_service.client.PaymentClient;
import dev.aj.order_service.mapper.ModelDtoMapper;
import dev.aj.order_service.model.common.PriceSummary;
import dev.aj.order_service.model.invoice.Invoice;
import dev.aj.order_service.model.order.Order;
import dev.aj.order_service.model.shipping.ShipmentStatus;
import dev.aj.order_service.orchestrator.OrderOrchestrator;
import dev.aj.order_service.orchestrator.OrderState;
import dev.aj.order_service.service.OrderValidatorService;
import dev.aj.order_service.service.PaymentService;
import dev.aj.order_service.service.PriceCalculatorService;
import dev.aj.order_service.service.ShippingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NullMarked;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@NullMarked
@Slf4j
@RequiredArgsConstructor
public class OrderOrchestratorImpl implements OrderOrchestrator {
    private final ModelDtoMapper modelDtoMapper;

    private final OrderValidatorService orderValidatorService;
    private final PriceCalculatorService priceCalculatorService;
    private final PaymentService paymentService;
    private final ShippingService shippingService;
    private final PaymentClient paymentClient;

    @Override
    public OrderState handle(OrderState.Placed placedOrder) {
        return this.orderValidatorService.validate(placedOrder);
    }

    @Override
    public OrderState handle(OrderState.Validated validatedOrder) {
        PriceSummary calculatedPriceSummary = this.priceCalculatorService.calculate(validatedOrder.order());
        return new OrderState.Priced(validatedOrder.order(), calculatedPriceSummary);
    }

    @Override
    public OrderState handle(OrderState.Priced priced) {
        Invoice invoice = this.paymentService.processPayment(priced.order(), priced.priceSummary());
        return new OrderState.Invoiced(priced.order(), invoice);
    }

    @Override
    public OrderState handle(OrderState.Invoiced invoicedOrder) {

        ShipmentStatus shipmentResponse = this.shippingService.createShipment(invoicedOrder.order());

        return switch (shipmentResponse) {
            case ShipmentStatus.Scheduled scheduled -> new OrderState.Shipped(
                    invoicedOrder.order(),
                    invoicedOrder.invoice(),
                    scheduled
            );

            case ShipmentStatus.Delivered delivered -> new OrderState.Completed(
                    invoicedOrder.order(),
                    invoicedOrder.invoice(),
                    Collections.singletonList(delivered.shipment())
            );

            case ShipmentStatus.Failed _ -> markOrderAsFailed(invoicedOrder);

            case ShipmentStatus.Cancelled _ -> new OrderState.Cancelled(invoicedOrder.order());

            case ShipmentStatus.Declined _ -> markOrderAsFailed(invoicedOrder);
        };
    }

    @Override
    public OrderState handle(OrderState.Cancelled cancelledOrder) {

        if (shippingService.canOrderBeCancelled(cancelledOrder.order().orderId())) {
            // call payment service to refund for the order
        }

        return null;
    }

    private static OrderState.Failed markOrderAsFailed(OrderState.Invoiced invoicedOrder) {
        return new OrderState.Failed(invoicedOrder.order(), invoicedOrder.invoice());
    }

    @Override
    public OrderState handle(OrderState.OrderCancellationRequested orderCancellationRequested) {

        Order order = orderCancellationRequested.order();

        if (shippingService.canOrderBeCancelled(order.orderId())) {
            return new OrderState.Cancelled(orderCancellationRequested.order());
        }

        log.warn("Order {} cannot be canceled", order.orderId());
        return orderCancellationRequested;
    }


    @Override
    public OrderState handle(OrderState.Shipped shippedOrder) {

        return new OrderState.Completed(
                shippedOrder.order(),
                shippedOrder.invoice(),
                List.of(shippedOrder.shipmentResponse().shipment()));
    }

}
