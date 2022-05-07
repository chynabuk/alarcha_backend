package com.project.alarcha.enums;

public enum AdditionalServiceOrderStatus {
    IN_PROCESS,
    CONFIRMED,
    CANCELLED;

    AdditionalServiceOrderStatus(){}

    @Override
    public String toString() {
        return this.name();
    }
}
