package dev.aj.design_patterns.behavioural.command_pattern.receiver;

import lombok.AllArgsConstructor;
import lombok.Builder;

@AllArgsConstructor
@Builder
public class PetFoodDispenser implements HomeGadget {

    private final String location;

    private boolean isOn;
    private boolean isPaused;

    @Override
    public String getLocation() {
        return this.location;
    }

    @Override
    public boolean isOn() {
        return isOn;
    }

    @Override
    public boolean isPaused() {
        return isPaused;
    }

    @Override
    public boolean turnOn() {
        if (isOn) {
            System.out.printf("%s - %s is already on%n", location, this.getClass().getSimpleName());
            return false;
        }
        System.out.printf("Turning on %s %s%n", location, this.getClass().getSimpleName());
        return true;
    }

    @Override
    public boolean turnOff() {
        if (!isOn) {
            System.out.printf("%s - %s is already off.%n", location, this.getClass().getSimpleName());
            return false;
        }

        System.out.printf("Turning off %s %s%n", location, this.getClass().getSimpleName());
        this.isOn = false;
        return true;
    }

    @Override
    public boolean pause() {
        if (isOn) {
            System.out.printf("Pausing on %s - %s%n", location, this.getClass().getSimpleName());
            this.isPaused = true;
            return true;
        }

        System.out.printf("%s can only be paused from ON.%n", this.getClass().getSimpleName());
        return false;
    }
}
