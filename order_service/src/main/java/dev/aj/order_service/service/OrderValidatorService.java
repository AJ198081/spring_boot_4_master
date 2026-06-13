package dev.aj.order_service.service;

import dev.aj.order_service.model.order.Order;
import dev.aj.order_service.model.order.OrderRequest;

@SuppressWarnings("unused")
public interface OrderValidatorService {
    Order validate(OrderRequest orderRequest);
}
