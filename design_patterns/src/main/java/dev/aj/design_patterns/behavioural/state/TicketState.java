package dev.aj.design_patterns.behavioural.state;

public interface TicketState {
    void startWork(Ticket ticket);
    void cancelWork(Ticket ticket);
    void completeWork(Ticket ticket);
    void restartWork(Ticket ticket);

    default Status getStatus(Ticket ticket) {
        return ticket.getStatus();
    }
}
