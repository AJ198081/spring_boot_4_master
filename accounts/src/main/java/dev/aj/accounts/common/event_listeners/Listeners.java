package dev.aj.accounts.common.event_listeners;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.resilience.retry.MethodRetryEvent;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Listeners {

    @EventListener(classes = {MethodRetryEvent.class})
    public void handleRetryEvent(MethodRetryEvent event) {
        log.warn(event.toString());
    }

}
