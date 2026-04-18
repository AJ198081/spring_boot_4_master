package dev.aj.design_patterns.behavioural.command_pattern.commands;

import java.util.ArrayDeque;
import java.util.Deque;

public abstract class AbstractSmartHomeCommand implements SmartHomeCommand {
    protected final Deque<Command> COMMANDS = new ArrayDeque<>();
    protected final Deque<Command> COMMAND_HISTORY = new ArrayDeque<>();

   public void undo() {
        if (COMMANDS.isEmpty()) {
            System.out.println("No commands to undo");
            return;
        }

        Command lastCommand = COMMANDS.pop();
        this.execute(lastCommand);
        COMMAND_HISTORY.push(lastCommand);
    }

    public void redo() {

        if (COMMAND_HISTORY.isEmpty()) {
            System.out.println("No commands to redo");
            return;
        }

        Command lastUndoCommand = COMMAND_HISTORY.pop();
        this.execute(lastUndoCommand);
        COMMANDS.push(lastUndoCommand);
    }

    public abstract void execute(Command command);

}
