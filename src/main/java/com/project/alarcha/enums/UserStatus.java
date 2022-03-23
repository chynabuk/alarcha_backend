package com.project.alarcha.enums;

public enum UserStatus {
    BLOCKED,
    DELETED,
    ACTIVE;

    UserStatus(){

    }

    @Override
    public String toString(){
        return this.name();
    }
}