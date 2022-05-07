package com.project.alarcha.enums;

public enum RoomStatus {
    ACTIVE,
    BOOKED,
    BLOCKED;

    RoomStatus(){

    }

    @Override
    public String toString(){
        return this.name();
    }
}
