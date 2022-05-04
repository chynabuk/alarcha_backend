package com.project.alarcha.enums;

public enum AdditionalServiceStatus {
    ACTIVE,
    BOOKED,
    BLOCKED;

    AdditionalServiceStatus(){}

    @Override
    public String toString() {
        return this.name();
    }
}
