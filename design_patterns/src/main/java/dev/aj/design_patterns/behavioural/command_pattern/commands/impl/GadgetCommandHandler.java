package dev.aj.design_patterns.behavioural.command_pattern.commands.impl;

import dev.aj.design_patterns.behavioural.command_pattern.commands.AbstractSmartHomeCommand;
import dev.aj.design_patterns.behavioural.command_pattern.commands.Command;
import dev.aj.design_patterns.behavioural.command_pattern.receiver.HomeGadget;

public final class GadgetCommandHandler extends AbstractSmartHomeCommand {

    private final HomeGadget homeGadget;

    public GadgetCommandHandler(HomeGadget homeGadget) {
        this.homeGadget = homeGadget;
    }

    @Override
    public void execute(Command command) {
        System.out.printf("%s %s is currently on: %s%n", homeGadget.getLocation(), homeGadget.getClass().getSimpleName(), homeGadget.isOn());
        if (homeGadget.runCommand(command)) {
            this.COMMANDS.push(command);
            System.out.printf("%s %s is now on: %s%n", homeGadget.getLocation(), homeGadget.getClass().getSimpleName(), homeGadget.isOn());
        }
    }
}
