package dev.aj.order_service.service;

import dev.aj.order_service.model.order.OrderRequest;
import dev.aj.order_service.orchestrator.OrderState;

@SuppressWarnings("unused")
public interface OrderValidatorService {
    OrderState validate(OrderRequest orderRequest);
}
