package dev.aj.design_patterns.behavioural.state.impl;

import dev.aj.design_patterns.behavioural.state.Status;
import dev.aj.design_patterns.behavioural.state.Ticket;
import dev.aj.design_patterns.behavioural.state.TicketState;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CompletedState implements TicketState {

    @Override
    public void startWork(Ticket ticket) {
        log.info("Ticket has been resolved successfully");
    }

    @Override
    public void cancelWork(Ticket ticket) {
        throw new IllegalStateException("Cannot cancel a ccompleted ticket");
    }

    @Override
    public void completeWork(Ticket ticket) {
        throw new IllegalStateException("Cannot complete an already ccompleted ticket");
    }

    @Override
    public void restartWork(Ticket ticket) {
        log.info("Ticket reopened from closed state");
        ticket.setStatus(Status.IN_PROGRESS);
        ticket.setCurrentState(new StartState());
    }

}
