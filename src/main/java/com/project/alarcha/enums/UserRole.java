package com.project.alarcha.enums;

public enum UserRole {
    CLIENT,
    ADMIN,
    SUPER_ADMIN;

    UserRole(){}

    @Override
    public String toString() {
        return this.name();
    }
}
