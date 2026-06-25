package dev.aj.stream_gatherers.integrators;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.stream.Gatherer;
import java.util.stream.Stream;

class FlatMapIntegratorTest {

    @Test
    void flatMapIntegratorTest() {

        Stream.of("Amarjit", "Jang", "Singh", "Bhandal")
                .gather(Gatherer.of(new FlatMapIntegrator<>(element -> Stream.of(Arrays.stream(element.split(""))))))
                .forEach(System.out::println);
    }

}