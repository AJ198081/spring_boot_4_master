package dev.aj.order_service.mapper;

import dev.aj.order_service.model.invoice.Invoice;
import dev.aj.order_service.model.order.Order;
import dev.aj.order_service.model.order.OrderItem;
import dev.aj.order_service.model.order.OrderResponse;
import dev.aj.order_service.model.product.Product;
import dev.aj.order_service.model.shipping.Shipment;
import dev.aj.order_service.model.shipping.ShipmentItem;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;
import java.util.stream.Stream;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
@SuppressWarnings("unused")
public interface ModelDtoMapper {

    default OrderResponse toOrderResponse(Order order, Invoice invoice, List<Shipment> shipments) {

        return new OrderResponse(
                order.orderId(),
                OrderResponse.InvoiceStatus.Due,
                order.items().stream()
                        .flatMap(this::mapOrderItemToShipmentItem)
                        .toList());
    }

    default Stream<ShipmentItem> mapOrderItemToShipmentItem(OrderItem orderItem) {
        return switch (orderItem.product()) {
            case Product.Single singleProduct ->
                    Stream.of(new ShipmentItem(singleProduct.productId(), singleProduct.name(), orderItem.quantity()));
            case Product.Bundle bundledProduct -> bundledProduct.items().stream()
                    .map(singleProduct -> new ShipmentItem(singleProduct.productId(), singleProduct.name(), orderItem.quantity()));
        };
    }

}
