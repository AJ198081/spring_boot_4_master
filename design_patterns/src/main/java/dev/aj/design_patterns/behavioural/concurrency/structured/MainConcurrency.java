package dev.aj.design_patterns.behavioural.concurrency.structured;

import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.ThreadFactory;

@Slf4j
public class MainConcurrency {

    static void main() {
        log.info("Starting the application");

//        runASingleLongRunningTask();

        ThreadFactory longRunningTaskFactory = Thread.ofVirtual()
                .name("long-running-task-", 1)
                .factory();

        try (StructuredTaskScope<TaskResponse, List<TaskResponse>> main = StructuredTaskScope.open(
//                StructuredTaskScope.Joiner.awaitAllSuccessfulOrThrow(),
                StructuredTaskScope.Joiner.allSuccessfulOrThrow(),
//                StructuredTaskScope.Joiner.awaitAll(),
                configuration -> configuration
                        .withThreadFactory(longRunningTaskFactory)
                        .withTimeout(Duration.ofSeconds(20)))) {

            var expensiveTask = new LongRunningTask("Expensive-task", 10, "100", 4);
            var cheapTask = new LongRunningTask("Cheap-task", 8, "10", 100);

            StructuredTaskScope.Subtask<TaskResponse> expensiveSubTask = main.fork(expensiveTask);
            StructuredTaskScope.Subtask<TaskResponse> cheapSubTask = main.fork(cheapTask);

            List<TaskResponse> completedTasks = main.join();

            completedTasks.forEach(taskResponse -> log.info(taskResponse.toString()));

            handleTaskOutput(expensiveSubTask);
            handleTaskOutput(cheapSubTask);

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (StructuredTaskScope.TimeoutException e) {
            log.error("Timeout occurred");
        } finally {
            log.info("Application completed");
        }
    }

    private static void handleTaskOutput(StructuredTaskScope.Subtask<TaskResponse> subtask) {

    // Check the state of the callable response object before calling the getter or exception
        if (subtask.state().equals(StructuredTaskScope.Subtask.State.SUCCESS)) {
            log.info(subtask.get().toString());
        }
        if (subtask.state().equals(StructuredTaskScope.Subtask.State.FAILED)) {
            log.error("Task failed with the message {}", subtask.exception().getMessage());
        }
    }

    @SuppressWarnings("unused")
    private static void runASingleLongRunningTask() {
        LongRunningTask longRunningTask = new LongRunningTask("Long-running", 10, "Done", 100);

        try (ExecutorService executorService = Executors.newFixedThreadPool(10)) {
            Future<TaskResponse> taskResponseFuture = executorService.submit(longRunningTask);

            Thread.sleep(Duration.ofSeconds(2));

            taskResponseFuture.cancel(true);

            log.info("Task response: {}", taskResponseFuture.get());

        } catch (Exception e) {
            log.error("Error occurred {}", e.getMessage());
        }
    }
}
