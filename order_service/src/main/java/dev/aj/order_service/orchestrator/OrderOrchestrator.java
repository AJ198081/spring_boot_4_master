package dev.aj.order_service.orchestrator;

@SuppressWarnings("unused")
public interface OrderOrchestrator {

    default OrderState orchestrate(OrderState orderState) {
        return switch (orderState) {
            case OrderState.Placed placed -> this.handle(placed);
            case OrderState.Validated validated -> this.handle(validated);
            case OrderState.Priced priced -> this.handle(priced);
            case OrderState.OrderCancellationRequested cancellationRequested -> this.handle(cancellationRequested);
            case OrderState.Cancelled cancelled -> this.handle(cancelled);
            case OrderState.Shipped shipped -> this.handle(shipped);
            case OrderState.Invoiced invoiced -> this.handle(invoiced);
            case OrderState.Completed completed -> completed;
            case OrderState.Failed failed -> failed;
        };
    }

    OrderState handle(OrderState.Placed placedOrder);
    OrderState handle(OrderState.Validated validatedOrder);
    OrderState handle(OrderState.Priced prices);
    OrderState handle(OrderState.OrderCancellationRequested cancellationRequest);
    OrderState handle(OrderState.Shipped shippedOrder);
    OrderState handle(OrderState.Invoiced invoicedOrder);
    OrderState handle(OrderState.Cancelled cancelledOrder);

}
