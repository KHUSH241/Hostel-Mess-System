package com.hms.model;
public abstract class User {
    private final String userId;
    private final String username;
    private final String password; 
    private final String fullName;

    public User(String userId, String username, String password, String fullName) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
    }

    public String getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getFullName() { return fullName; }

    public boolean checkPassword(String candidate) {
        return this.password != null && this.password.equals(candidate);
    }

    protected String getRawPassword() { return password; }

    public abstract String getRole();

    public abstract String toCsvLine();
}
