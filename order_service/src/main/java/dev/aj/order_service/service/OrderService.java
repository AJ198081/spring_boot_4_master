package dev.aj.order_service.service;

import dev.aj.order_service.model.order.OrderRequest;
import dev.aj.order_service.model.order.OrderResponse;
import dev.aj.order_service.orchestrator.OrderState;
import org.jspecify.annotations.Nullable;

import java.util.UUID;

@SuppressWarnings("unused")
public interface OrderService {

    OrderResponse createOrder(OrderRequest orderRequest);

    @Nullable OrderState getCurrentState(UUID orderId);

    Boolean cancelOrder(UUID orderId);
}
