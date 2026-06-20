package dev.aj.order_service.service;

import dev.aj.order_service.client.CustomerClient;
import dev.aj.order_service.client.ProductClient;
import dev.aj.order_service.model.order.Order;
import dev.aj.order_service.model.order.OrderItem;
import dev.aj.order_service.model.order.OrderRequest;
import dev.aj.order_service.model.order.OrderResponse;
import dev.aj.order_service.model.product.ProductStatus;
import dev.aj.order_service.orchestrator.OrderOrchestrator;
import dev.aj.order_service.orchestrator.OrderState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

        OrderState orderState = new OrderState.Placed(new Order(UUID.randomUUID(), customerClient.getCustomer(orderRequest.customerId()), orderItems, LocalDate.now()));

        while (!isTerminalState(orderState)) {
            orderState = orderOrchestrator.orchestrate(orderState);
        }

        return new OrderResponse(orderState.order().orderId(), null, null);
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

