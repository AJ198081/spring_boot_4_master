package dev.aj.design_patterns.behavioural.command_pattern;

import dev.aj.design_patterns.behavioural.command_pattern.commands.AbstractSmartHomeCommand;
import dev.aj.design_patterns.behavioural.command_pattern.commands.Command;
import dev.aj.design_patterns.behavioural.command_pattern.commands.impl.GadgetCommandHandler;
import dev.aj.design_patterns.behavioural.command_pattern.receiver.Heater;
import dev.aj.design_patterns.behavioural.command_pattern.receiver.Light;
import dev.aj.design_patterns.behavioural.command_pattern.receiver.PetFoodDispenser;

public class CommandApplication {

    static void main(String[] args) {

        Light bedroomLight = new Light("Bedroom", true, false);
        Heater kitchenHeater = new Heater("Kitchen", true, false);
        PetFoodDispenser garagePetFoodDispenser = PetFoodDispenser.builder()
                .location("Garage")
                .isOn(true)
                .isPaused(false)
                .build();

        AbstractSmartHomeCommand command1 = new GadgetCommandHandler(garagePetFoodDispenser);
        command1.execute(Command.OFF);
    }

}
