package edu.lysak.antifraud.domain.transaction;

public enum RejectionReason {
    NONE("none"),
    AMOUNT("amount"),
    CARD_NUMBER("card-number"),
    IP("ip");

    private final String reason;

    RejectionReason(String reason) {
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }
}
