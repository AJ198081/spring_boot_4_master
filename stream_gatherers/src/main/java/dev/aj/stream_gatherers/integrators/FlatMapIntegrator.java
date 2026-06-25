package dev.aj.stream_gatherers.integrators;

import lombok.RequiredArgsConstructor;

import java.util.function.Function;
import java.util.stream.Gatherer;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class FlatMapIntegrator<T, R> implements Gatherer.Integrator<Void, T, Stream<R>> {

    private final Function<T, Stream<R>> function;

    @Override
    public boolean integrate(Void state, T element, Gatherer.Downstream<? super Stream<R>> downstream) {
        downstream.push(function.apply(element));
        return !downstream.isRejecting();
    }
}
