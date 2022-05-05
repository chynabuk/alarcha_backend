package com.project.alarcha.enums;

public enum RoomOrderStatus {
    IN_PROCESS,
    CONFIRMED,
    CANCELLED,
    DECLINED;

    RoomOrderStatus(){}

    @Override
    public String toString(){
        return this.name();
    }
}
