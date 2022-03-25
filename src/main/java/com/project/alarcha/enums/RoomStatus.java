package com.project.alarcha.enums;

public enum RoomStatus {
    ACTIVE,
    BLOCKED;

    RoomStatus(){

    }

    @Override
    public String toString(){
        return this.name();
    }
}
