package dev.aj.design_patterns.behavioural.command_pattern.receiver;

import dev.aj.design_patterns.behavioural.command_pattern.commands.Command;

public interface HomeGadget {

    String getLocation();

    boolean isOn();

    boolean isPaused();

    boolean turnOn();

    default boolean runCommand(Command command) {
        return switch (command) {
            case ON -> this.turnOn();
            case OFF -> this.turnOff();
            case PAUSE -> this.pause();
        };
    }

    boolean turnOff();

    boolean pause();
}
