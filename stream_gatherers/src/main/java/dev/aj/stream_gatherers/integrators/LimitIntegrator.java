package dev.aj.stream_gatherers.integrators;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.stream.Gatherer;

@Slf4j
@Component
public class LimitIntegrator implements Gatherer.Integrator<Void, Integer, Integer> {

    @Value("${limit}")
    private int limit;

    @Override
    public boolean integrate(Void state, Integer element, Gatherer.Downstream<? super Integer> downstream) {

        if (element <= limit && !downstream.isRejecting()) {
            downstream.push(element);
            return true;
        }

        log.info("Integrating element: {}, but won't be processed downstream.", element);

        return false;
    }
}
