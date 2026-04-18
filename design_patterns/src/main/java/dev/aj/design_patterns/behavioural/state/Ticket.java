package dev.aj.design_patterns.behavioural.state;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ticket {

    private Integer id;
    private String title;
    private String description;
    private TicketState currentState;

    private Status previousStatus;
    private Status status;

    @Builder.Default
    private List<String> comments = new ArrayList<>();

    public void addComment(String comment) {
        comments.add(comment);
    }

    public void startTicket() {
        currentState.startWork(this);
    }

    public void cancelTicket() {
        currentState.cancelWork(this);
    }

    public void resolveTicket() {
        currentState.completeWork(this);
    }

    public void reopenTicket() {
        currentState.restartWork(this);
    }

    public void transitionToState(TicketState newState) {
        previousStatus = status;
        currentState = newState;
    }

}
