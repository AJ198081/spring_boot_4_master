package dev.aj.order_service.service;

import dev.aj.order_service.model.common.PriceSummary;
import dev.aj.order_service.model.order.Order;

public interface PriceCalculatorService {

    PriceSummary calculate(Order order);

}
