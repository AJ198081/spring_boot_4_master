package dev.aj.stream_gatherers.integrators;

import lombok.RequiredArgsConstructor;

import java.util.function.Predicate;
import java.util.stream.Gatherer;

@RequiredArgsConstructor
public class TakeWhileIntegrator implements Gatherer.Integrator<Void, Integer, Integer>{

    private final Predicate<Integer> predicate;

    @Override
    public boolean integrate(Void state, Integer element, Gatherer.Downstream<? super Integer> downstream) {

        if (predicate.test(element)) {
            return downstream.push(element);
        }

        return !downstream.isRejecting();
    }
}
