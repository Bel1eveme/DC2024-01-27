package com.poluectov.reproject.discussion.service.kafka;

public enum KafkaMessageType {
    GET_ALL("GET_ALL"),
    SAVE("SAVE"),
    DELETE("DELETE"),
    ONE("ONE"),
    UPDATE("UPDATE");

    private final String value;

    KafkaMessageType(String value) {
        this.value = value;
    }

    public KafkaMessageType fromValue(String value) {
        for (KafkaMessageType type : KafkaMessageType.values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid value: " + value);
    }
}
