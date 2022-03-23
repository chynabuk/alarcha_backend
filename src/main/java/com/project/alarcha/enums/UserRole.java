package com.project.alarcha.enums;

public enum UserRole {
    CLIENT,
    MANAGER,
    ADMIN;

    UserRole(){}

    @Override
    public String toString() {
        return this.name();
    }
}
