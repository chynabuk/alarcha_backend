package com.project.alarcha.enums;

public enum TimeType {
    TIME,
    DATE;

    TimeType() {
    }

    @Override
    public String toString(){
        return this.name();
    }

}