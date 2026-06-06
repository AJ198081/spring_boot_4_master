package dev.aj.graphql.services;

import dev.aj.graphql.model.entities.Order;
import dev.aj.graphql.model.entities.OrderItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.resource.ResourceUrlProvider;

import java.time.Instant;
import java.util.List;
import java.util.stream.IntStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final Faker faker;
    private final ResourceUrlProvider resourceUrlProvider;

    public Order generateARandomOrder() {
        return Order.create(
                faker.random().nextLong(),
                generateOrderItems(faker.random().nextInt(1, 4)),
                Order.Status.NEW,
                Instant.now()
        );
    }

    private List<OrderItem> generateOrderItems(int quantity) {

        return IntStream.rangeClosed(0, quantity)
                .mapToObj(_ -> generateARandomOrderItem())
                .toList();
    }

    private OrderItem generateARandomOrderItem() {
        return new OrderItem(
                    faker.commerce().brand(),
                    faker.commerce().productName(),
                    Double.valueOf(faker.commerce().price()),
                    faker.number().numberBetween(1, 10)
            );
    }

}
