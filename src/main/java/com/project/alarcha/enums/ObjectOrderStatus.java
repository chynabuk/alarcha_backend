package com.project.alarcha.enums;

public enum ObjectOrderStatus {
    IN_PROCESS,
    CONFIRMED,
    CANCELLED;

    ObjectOrderStatus(){}

    @Override
    public String toString() {
        return this.name();
    }
}
