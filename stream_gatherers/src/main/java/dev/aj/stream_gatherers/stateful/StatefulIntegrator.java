package dev.aj.stream_gatherers.stateful;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Gatherer;

public class StatefulIntegrator {

    private static final Logger logger = LoggerFactory.getLogger(StatefulIntegrator.class);

    public static <T> Gatherer<T, ?, T> limit(int maxSize) {
        return Gatherer.ofSequential(() -> new AtomicInteger(0),
                (AtomicInteger state, T element, Gatherer.Downstream<? super T> downstream) -> {

                    if (state.get() < maxSize) {
                        logElement(element);
                        state.incrementAndGet();
                        return element != null && downstream.push(element);
                    }

                    return false;
                });
    }

    public static <T> Gatherer<T, ?, T> distinct() {
        return Gatherer.ofSequential(
                HashSet::new,
                (HashSet<T> state, T element, Gatherer.Downstream<? super T> downstream) -> {
                    logElement(element);
                    if (!state.contains(element)) {
                        state.add(element);
                        return downstream.push(element);
                    }
                    return true;
                });
    }

/*public static <T extends Number> Gatherer<T, T, Double> movingAverage(Window windowSize) {

    return Gatherer.ofSequential(
            (Supplier<Deque<T>>) ArrayDeque::new,
            (Deque<T> state, T element, Gatherer.Downstream<Double> downstream) -> {

                // Maintain fixed-size sliding window
                if (state.size() >= windowSize.size()) {
                    state.removeFirst();
                }
                state.addLast(element);

                // Compute average
                double average = state.stream()
                        .mapToDouble(Number::doubleValue)
                        .average()
                        .orElse(0.0);

                downstream.push(average);

                return downstream.isRejecting();
            }
    );
}*/

    private static <T> void logElement(T element) {
        logger.info("Processing element: {}", element);
    }

}
