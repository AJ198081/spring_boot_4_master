package dev.aj.design_patterns.behavioural.state.impl;

import dev.aj.design_patterns.behavioural.state.Status;
import dev.aj.design_patterns.behavioural.state.Ticket;
import dev.aj.design_patterns.behavioural.state.TicketState;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NewState implements TicketState {

    @Override
    public void startWork(Ticket ticket) {
        log.info("Processing ticket {}", ticket.getId());
        ticket.setStatus(Status.PENDING);
        ticket.setCurrentState(new StartState());
    }

    @Override
    public void cancelWork(Ticket ticket) {
        throw new IllegalStateException("Cannot cancel a new ticket");
    }

    @Override
    public void completeWork(Ticket ticket) {
        throw new IllegalStateException("Cannot resolve a new ticket");
    }

    @Override
    public void restartWork(Ticket ticket) {
        throw new IllegalStateException("Cannot reopen a new ticket");
    }
}
