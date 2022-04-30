package com.project.alarcha.enums;

public enum RoomOrderStatus {
    IN_PROCESS,
    CONFIRMED,
    CANCELLED;

    RoomOrderStatus(){}

    @Override
    public String toString(){
        return this.name();
    }
}
