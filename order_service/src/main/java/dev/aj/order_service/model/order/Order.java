package dev.aj.order_service.model.order;

import dev.aj.order_service.model.customer.Customer;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record Order(UUID orderId, Customer customer, List<OrderItem> items, LocalDate createdDate) {
}
