package com.hms.model;

public class Admin extends User {
    private final String designation;

    public Admin(String userId, String username, String password, String fullName, String designation) {
        super(userId, username, password, fullName);
        this.designation = designation;
    }

    public String getDesignation() { return designation; }

    @Override
    public String getRole() { return "ADMIN"; }

    @Override
    public String toCsvLine() {
        return String.join("|", "ADMIN", getUserId(), getUsername(), getRawPassword(),
                getFullName(), designation);
    }
}
