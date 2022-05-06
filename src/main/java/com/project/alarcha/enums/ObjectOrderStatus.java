package com.project.alarcha.enums;

public enum ObjectOrderStatus {
    IN_PROCESS,
    CONFIRMED,
    CANCELLED,
    DECLINED;

    ObjectOrderStatus(){}

    @Override
    public String toString() {
        return this.name();
    }
}
