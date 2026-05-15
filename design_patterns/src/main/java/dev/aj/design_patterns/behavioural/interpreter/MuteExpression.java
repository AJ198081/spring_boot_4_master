package dev.aj.design_patterns.behavioural.interpreter;

import lombok.AllArgsConstructor;

@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class MuteExpression implements Expression {

    private String userName;

    public static MuteExpression forUser(String userName) {
        return new MuteExpression(userName);
    }

    @Override
    public void interpret(ChatContext context) {
        System.out.printf("%s muted %s%n", context.getCurrentUser(), userName);
    }
}
