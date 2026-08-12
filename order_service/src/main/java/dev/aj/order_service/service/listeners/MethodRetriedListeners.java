package dev.aj.order_service.service.listeners;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.resilience.retry.MethodRetryEvent;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MethodRetriedListeners {


    @EventListener(MethodRetryEvent.class)
    public void onMethodRetried(MethodRetryEvent event) {

      log.info("Method retried {}", event);

        if (event.isRetryAborted()) {
            log.info("Retry aborted {}", event.getFailure().getMessage());
        }
    }
}
