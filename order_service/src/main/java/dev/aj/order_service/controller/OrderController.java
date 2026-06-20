package dev.aj.order_service.controller;

import dev.aj.order_service.model.order.OrderRequest;
import dev.aj.order_service.model.order.OrderResponse;
import dev.aj.order_service.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import java.util.UUID;

@RestController
@RequestMapping("${order_service.uri}")
@RequiredArgsConstructor
@Slf4j
@SuppressWarnings("unused")
public class OrderController {

    private final OrderService orderService;


    @PostMapping
    public ResponseEntity<OrderResponse> placeOrder(@Validated @RequestBody OrderRequest orderRequest, ServletWebRequest request) {
        log.info("URI to order service: {}", request.getRequest().getRequestURL());
        return ResponseEntity.ok(orderService.createOrder(orderRequest));
    }

    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<Boolean> cancelOrder(@PathVariable UUID orderId) {

        return ResponseEntity.ok(orderService.cancelOrder(orderId));
    }


}
