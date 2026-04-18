package dev.aj.design_patterns.behavioural.state;

import dev.aj.design_patterns.behavioural.state.impl.NewState;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TicketStateTest {

    @Test
    void test() {

        Ticket testTicket = Ticket.builder()
                .id((int) Math.ceil(Math.random() * 1000))
                .title("Test Ticket")
                .description("Test Ticket")
                .currentState(new NewState())
                .build();

        testTicket.getCurrentState().startWork(testTicket);

        assertEquals(Status.PENDING, testTicket.getStatus());

        testTicket.getCurrentState().startWork(testTicket);
        assertEquals(Status.IN_PROGRESS, testTicket.getStatus());

        testTicket.getCurrentState().startWork(testTicket);
        assertEquals(Status.COMPLETED, testTicket.getStatus());
    }

}