package dev.aj.stream_gatherers.integrators;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.stream.Gatherer;

@Component
@Slf4j
public class FilterIntegrator implements Gatherer.Integrator<Void, Integer, Integer> {

    @Override
    public boolean integrate(Void state, Integer element, Gatherer.Downstream<? super Integer> downstream) {

        if (element % 2 == 0) {
            downstream.push(element);
        }

        return true;
    }
}
