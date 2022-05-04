package com.project.alarcha.enums;

public enum OrderStatus {
    IN_PROCESS,
    CONFIRMED,
    CANCELLED;

    OrderStatus(){

    }

    @Override
    public String toString(){
        return this.name();
    }
}
