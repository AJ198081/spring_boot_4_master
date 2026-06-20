package dev.aj.order_service.service;

import dev.aj.order_service.orchestrator.OrderState;

@SuppressWarnings("unused")
public interface OrderValidatorService {
    OrderState validate(OrderState.Placed orderRequest);
}
