package dev.aj.graphql.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Controller
@Slf4j
public class GraphQLController {

    ThreadLocal<List<Integer>> threadLocalNumbers = ThreadLocal.withInitial(ArrayList::new);

    @QueryMapping("greetings")
    public String sayHello() {
        logThreadName();
        return "Hello World!";
    }

    @SchemaMapping(typeName = "Query", value = "custom_greetings")
    public String sayCustomHello(@Argument String name) {
        logThreadName();
        return "Hello %s!".formatted(name);
    }

    private static void logThreadName() {
        log.info("Current thread: {}", Thread.currentThread().getName());
    }

    @QueryMapping(value = "random_integer")
    public List<Integer> getRandomInteger() {
        int generatedInteger = ThreadLocalRandom.current().nextInt(100);

        log.info("For thread: {}, Generated random integer: {}", Thread.currentThread().getName(), generatedInteger);
        threadLocalNumbers.get().add(generatedInteger);

        return threadLocalNumbers.get();
    }
}
