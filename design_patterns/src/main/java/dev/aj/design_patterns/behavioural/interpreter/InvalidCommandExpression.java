package dev.aj.design_patterns.behavioural.interpreter;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class InvalidCommandExpression implements Expression {

    private String command;

    @Override
    public void interpret(ChatContext context) {
        System.out.printf("%s: %s is not a valid command%n", context.getCurrentUser(), command);
    }
}
