package com.stctest.anycompmarketplace.enums;


public enum RoleType {
    ADMIN("ADMIN"), SELLER("SELLER"), BUYER("BUYER");

    private final String name;

    RoleType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return "ROLE_" + this.name();
    }

}