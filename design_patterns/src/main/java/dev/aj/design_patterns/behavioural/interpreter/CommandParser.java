package dev.aj.design_patterns.behavioural.interpreter;

public class CommandParser {

    public static Expression parse(String command) {
        String trimmedCommand = command.toLowerCase().trim();

        String commandName = trimmedCommand.split(" ")[0];
        String argument = trimmedCommand.split(" ")[1];

        return switch (commandName) {
            case "join" -> new JoinExpression(argument);
            case "mute" -> MuteExpression.forUser(argument);
            case "remind" -> RemindExpression.withText(argument);
            default -> new InvalidCommandExpression(command);
        };
    }

}
