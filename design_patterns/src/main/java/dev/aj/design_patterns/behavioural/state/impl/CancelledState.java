package dev.aj.design_patterns.behavioural.state.impl;

import dev.aj.design_patterns.behavioural.state.Status;
import dev.aj.design_patterns.behavioural.state.Ticket;
import dev.aj.design_patterns.behavioural.state.TicketState;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CancelledState implements TicketState {

    @Override
    public void startWork(Ticket ticket) {
        throw new IllegalStateException("Cannot process a cancelled ticket");
    }

    @Override
    public void cancelWork(Ticket ticket) {
        throw new IllegalStateException("Cannot cancel a cancelled ticket");
    }

    @Override
    public void completeWork(Ticket ticket) {
        throw new IllegalStateException("Cannot resolve a cancelled ticket");
    }

    @Override
    public void restartWork(Ticket ticket) {
        log.info("Ticket {} reopened from cancelled state", ticket.getId());
        ticket.setStatus(Status.PENDING);
        ticket.setCurrentState(new StartState());
    }
}
