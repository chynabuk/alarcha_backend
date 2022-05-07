package com.project.alarcha.enums;

public enum ObjectStatus {
    ACTIVE,
    BOOKED,
    BLOCKED;

    ObjectStatus(){}

    @Override
    public String toString() {
        return this.name();
    }
}
