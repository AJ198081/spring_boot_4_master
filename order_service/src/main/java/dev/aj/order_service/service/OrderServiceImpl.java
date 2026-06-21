package dev.aj.order_service.service;

import dev.aj.order_service.client.CouponClient;
import dev.aj.order_service.client.CustomerClient;
import dev.aj.order_service.client.ProductClient;
import dev.aj.order_service.model.coupon.Coupon;
import dev.aj.order_service.model.invoice.Invoice;
import dev.aj.order_service.model.order.Order;
import dev.aj.order_service.model.order.OrderItem;
import dev.aj.order_service.model.order.OrderRequest;
import dev.aj.order_service.model.order.OrderResponse;
import dev.aj.order_service.model.product.ProductStatus;
import dev.aj.order_service.model.shipping.ShipmentItem;
import dev.aj.order_service.orchestrator.OrderOrchestrator;
import dev.aj.order_service.orchestrator.OrderState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.UUID;


@Service
@SuppressWarnings("unused")
@Slf4j
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderOrchestrator orderOrchestrator;
    private final CustomerClient customerClient;
    private final ProductClient productClient;
    private final CouponClient couponClient;

    @Override
    public OrderResponse createOrder(OrderRequest orderRequest) {

        ProductStatus fetchedProductStatus = productClient.getProduct(orderRequest.productId());

        if (!(fetchedProductStatus instanceof ProductStatus.Active)) {
            throw new IllegalStateException("Product is not active");
        }

        List<OrderItem> orderItems = List.of(new OrderItem(
                ((ProductStatus.Active) fetchedProductStatus).product(),
                orderRequest.quantity()
        ));

        Coupon validCoupon = couponClient.getCoupon(orderRequest.coupon());

        OrderState orderState = new OrderState.Placed(new Order(
                UUID.randomUUID(),
                customerClient.getCustomer(orderRequest.customerId()),
                orderItems,
                LocalDate.now(),
                validCoupon)
        );

        while (!isTerminalState(orderState)) {
            orderState = orderOrchestrator.orchestrate(orderState);
        }

        return switch (orderState) {
            case OrderState.Completed completed -> new OrderResponse(
                    completed.order().orderId(),
                    OrderResponse.OrderStatus.COMPLETED,
                    invoiceStatus(completed.invoice()),
                    completed.invoice().priceSummary().total().amount(),
                    shipmentItems(completed.order().items())
            );
            case OrderState.Cancelled cancelled -> new OrderResponse(
                    cancelled.order().orderId(),
                    OrderResponse.OrderStatus.CANCELLED,
                    OrderResponse.InvoiceStatus.CANCELLED,
                    BigDecimal.ZERO,
                    Collections.emptyList()
            );
            case OrderState.Failed failed -> new OrderResponse(
                    failed.order().orderId(),
                    invoiceStatus(failed.invoice()),
                    failed.invoice().priceSummary().total().amount(),
                   Collections.emptyList());
            default -> throw new IllegalStateException("Unexpected terminal order state: " + orderState);
        };
    }

    private List<ShipmentItem> shipmentItems(List<OrderItem> items) {
        return items.stream()
                .map(item -> new ShipmentItem(item.product().productId(), item.product().name(), item.quantity()))
                .toList();
    }

    private OrderResponse.InvoiceStatus invoiceStatus(Invoice invoice) {
        return switch (invoice) {
            case Invoice.Paid paid -> OrderResponse.InvoiceStatus.Paid;
            case Invoice.Unpaid unpaid -> OrderResponse.InvoiceStatus.Due;
        };
    }

    private boolean isTerminalState(OrderState orderState) {
        return orderState instanceof OrderState.Cancelled
                || orderState instanceof OrderState.Completed
                || orderState instanceof OrderState.Failed;
    }

    @Override
    public OrderState getCurrentState(UUID orderId) {
        return null;
    }

    @Override
    public Boolean cancelOrder(UUID orderId) {
        return null;
    }
}

