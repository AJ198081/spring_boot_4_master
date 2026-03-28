package dev.aj.design_patterns.behavioural.memento;

import java.util.ArrayDeque;
import java.util.Deque;

public class QueryHistory {
    private static final int MAX_HISTORY_SIZE = 10;
    private final Deque<QueryState.QuerySnapshot> undoStack = new ArrayDeque<>(MAX_HISTORY_SIZE);
    private final Deque<QueryState.QuerySnapshot> redoStack = new ArrayDeque<>(MAX_HISTORY_SIZE);

    public void snapshotQueryState(QueryState stateToBeSnapshot) {
        pushUnbounded(undoStack, QueryState.snapshot(stateToBeSnapshot));
        redoStack.clear();
    }

    public void undo(QueryState currentState) {
        pushUnbounded(redoStack, QueryState.snapshot(currentState));
        QueryState.QuerySnapshot snapshot = undoStack.removeLast();
        currentState.restore(snapshot);
    }

    public void redo(QueryState currentState) {
        if (redoStack.isEmpty()) {
            return;
        }

        pushUnbounded(undoStack, QueryState.snapshot(currentState));
        currentState.restore(redoStack.pop());
    }

    private void pushUnbounded(Deque<QueryState.QuerySnapshot> stack, QueryState.QuerySnapshot snapshot) {
        if (stack.size() == MAX_HISTORY_SIZE) {
            stack.removeFirst();
        }
        stack.addLast(snapshot);
    }
}
