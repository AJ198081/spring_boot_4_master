package dev.aj.stream_gatherers.integrators;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Predicate;
import java.util.stream.Gatherer;

/*
* Greedy Integrator is used to process all elements so long as the downstream doesn't want to reject them
* It has no intention of its own to short-circuit the processing
* It is just a hint to Java, so the Stream processing can do any of its own optimisation, if it downstream processing requires
*
* A non-greedy integrator will stop Java from doing its own optimisation
* */
@RequiredArgsConstructor
public class GenericFilterGreedyIntegrator<T> implements Gatherer.Integrator.Greedy<Void, T, T> {

    private final Predicate<T> predicate;

    Logger logger = LoggerFactory.getLogger(GenericFilterGreedyIntegrator.class);

    @Override
    public boolean integrate(Void state, T element, Gatherer.Downstream<? super T> downstream) {

//        logger.info("Processing element: {}, in the greedy generic integrator.", element);

        if (element != null && predicate.test(element)) {
           return downstream.push(element); // Downstream might want to reject element, so we need to check that too.
        }
        return true;
    }

}
