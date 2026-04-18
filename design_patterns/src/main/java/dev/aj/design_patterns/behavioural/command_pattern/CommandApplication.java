package dev.aj.design_patterns.behavioural.command_pattern;

import dev.aj.design_patterns.behavioural.command_pattern.commands.AbstractSmartHomeCommand;
import dev.aj.design_patterns.behavioural.command_pattern.commands.impl.GadgetCommandHandler;
import dev.aj.design_patterns.behavioural.command_pattern.receiver.Heater;
import dev.aj.design_patterns.behavioural.command_pattern.receiver.Light;
import dev.aj.design_patterns.behavioural.command_pattern.receiver.PetFoodDispenser;

import static dev.aj.design_patterns.behavioural.command_pattern.commands.Command.OFF;
import static dev.aj.design_patterns.behavioural.command_pattern.commands.Command.PAUSE;

public class CommandApplication {

    static void main(String[] args) {

        Light bedroomLight = new Light("Bedroom", true, false);
        Heater kitchenHeater = new Heater("Kitchen", true, false);
        PetFoodDispenser garagePetFoodDispenser = PetFoodDispenser.builder()
                .location("Garage")
                .isOn(true)
                .isPaused(false)
                .build();

        AbstractSmartHomeCommand command = new GadgetCommandHandler(garagePetFoodDispenser);
        command.execute(OFF);
        command.execute(PAUSE);
        command.undo();
        command.redo();
        command.execute(PAUSE);
        command.execute(OFF);
    }

}
