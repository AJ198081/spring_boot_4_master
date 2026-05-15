package dev.aj.design_patterns.behavioural.interpreter;

import lombok.AllArgsConstructor;

@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class RemindExpression implements Expression {

    private String reminderText;

    public static RemindExpression withText(String reminderText) {
        return new RemindExpression(reminderText);
    }

    @Override
    public void interpret(ChatContext context) {
        System.out.printf("%s, Remember: %s%n", context.getCurrentUser(), this.reminderText);
    }
}
