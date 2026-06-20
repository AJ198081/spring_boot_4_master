package dev.aj.order_service.model.common;

public record Mobile(String mobile) {

    public Mobile {

        if (mobile == null || mobile.isBlank()) {
            throw new IllegalArgumentException("Mobile is mandatory");
        }

        mobile = mobile.replaceAll("[^0-9]", "");

        if (mobile.length() > 14) {
            throw new IllegalArgumentException("Mobile must be at most 14 digits");
        }
    }
}
