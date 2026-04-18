package dev.aj.design_patterns.behavioural.state.impl;

import dev.aj.design_patterns.behavioural.state.Status;
import dev.aj.design_patterns.behavioural.state.Ticket;
import dev.aj.design_patterns.behavioural.state.TicketState;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InProgressState implements TicketState {

    @Override
    public void startWork(Ticket ticket) {
        ticket.setStatus(Status.COMPLETED);
        ticket.setCurrentState(new CompletedState());
    }

    @Override
    public void cancelWork(Ticket ticket) {
        log.info("Cancelling ticket {}", ticket.getId());
        ticket.setStatus(Status.CANCELLED);
        ticket.setCurrentState(new CancelledState());
    }

    @Override
    public void completeWork(Ticket ticket) {
        log.info("Resolving ticket {}", ticket.getId());
        ticket.setStatus(Status.COMPLETED);
        ticket.setCurrentState(new CompletedState());
    }

    @Override
    public void restartWork(Ticket ticket) {
        throw new IllegalStateException("Cannot reopen an in-progress ticket");
    }

}
