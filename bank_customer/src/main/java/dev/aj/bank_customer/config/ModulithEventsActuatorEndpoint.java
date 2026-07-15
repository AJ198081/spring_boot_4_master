package dev.aj.bank_customer.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.Selector;
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation;
import org.springframework.context.annotation.Configuration;
import org.springframework.modulith.events.IncompleteEventPublications;
import org.springframework.modulith.events.ResubmissionOptions;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Configuration
@Endpoint(id = "trigger-events")
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class ModulithEventsActuatorEndpoint {

    private final IncompleteEventPublications incompleteEventPublications;

    @ReadOperation
    public Map<String, String> retriggerIncompleteEvents() {

        List<String> retriggeredEvents = new ArrayList<>();

        incompleteEventPublications.resubmitIncompletePublications(
                ResubmissionOptions.defaults()
                        .withFilter(eventPublication -> eventPublication.getCompletionAttempts() <= 3)
                        .withBatchSize(10)
                        .withMinAge(Duration.ofMinutes(5L))
                        .withMaxInFlight(5)
        );

        return Map.of("status", "Retriggered all that are tried at most 15 times");
    }

    @WriteOperation
    public String triggerIndividualEvent(
            @Selector UUID eventIdentifier
    ) {

        incompleteEventPublications.resubmitIncompletePublications(
                ResubmissionOptions.defaults()
                        .withFilter(eventPublication ->
                                eventPublication.getIdentifier().equals(eventIdentifier))
        );

        return "Re-triggered event identifier -> %s".formatted(eventIdentifier);
    }

}
