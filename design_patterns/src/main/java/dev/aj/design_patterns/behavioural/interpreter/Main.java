package dev.aj.design_patterns.behavioural.interpreter;

public class Main {
    static void main() {
        System.out.println("----- Interpreter Pattern -----");
        Expression expression = CommandParser.parse("join social/political");

        ChatContext context = new ChatContext("AJ");

        expression.interpret(context);

        Expression expression2 = CommandParser.parse("mute AJ");

        expression2.interpret(context);

    }
}
