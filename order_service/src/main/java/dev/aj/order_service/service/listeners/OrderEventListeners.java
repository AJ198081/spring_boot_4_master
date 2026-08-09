package dev.aj.order_service.service.listeners;

import dev.aj.order_service.orchestrator.OrderState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderEventListeners {


    @ApplicationModuleListener
    public void on(OrderState.Placed orderPlacedEvent) {
        log.info("Begin orchestration for order: {}", orderPlacedEvent);
    }

    @ApplicationModuleListener
    public void on(OrderState.FailedToCancel orderStateFailedToCancel) {

        log.info("Failed to cancel order: {}", orderStateFailedToCancel);
    }


}
