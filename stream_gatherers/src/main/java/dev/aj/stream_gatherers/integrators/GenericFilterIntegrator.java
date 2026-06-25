package dev.aj.stream_gatherers.integrators;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Predicate;
import java.util.stream.Gatherer;

@RequiredArgsConstructor
public class GenericFilterIntegrator<T> implements Gatherer.Integrator<Void, T, T> {

    private final Predicate<T> predicate;

    Logger logger = LoggerFactory.getLogger(GenericFilterIntegrator.class);

    @Override
    public boolean integrate(Void state, T element, Gatherer.Downstream<? super T> downstream) {

        logger.info("Processing element: {}, in the generic integrator.", element);

        if (element != null && predicate.test(element)) {
            return downstream.push(element);
        }

        return true;
    }
}
