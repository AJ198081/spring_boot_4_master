package dev.aj.order_service.model.common;

public record ABN(String abn) {
    public ABN {
        if (abn == null || abn.isBlank()) {
            throw new IllegalArgumentException("ABN cannot be null");
        }
        if (abn.length() != 11) {
            throw new IllegalArgumentException("ABN must be 11 characters long");
        }
    }
}
