package dev.aj.accounts.common.domain.dtos.mappers;

public class MapperHelpers {

    public static String purgeDashesAndSpaces(String input) {
        return input.replaceAll("-", "")
                .replaceAll("\\s", "");
    }

    public static String purgeString(String input, String... args) {

        for (String arg : args) {
            input = input.replaceAll(arg, "");
        }

        return input;
    }
}
