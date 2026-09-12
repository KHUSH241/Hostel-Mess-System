package com.hms.model;

/**
 * Base class for anyone who can log into the system.
 * Demonstrates abstraction + inheritance (Student / Admin specialize this).
 */
public abstract class User {
    private final String userId;
    private final String username;
    private final String password; // stored as-is for a college-project scope; see README for note on hashing
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

    /** Available to subclasses only (e.g. for CSV persistence) - never exposed publicly as plain text. */
    protected String getRawPassword() { return password; }

    public abstract String getRole();

    /** Serialize to a pipe-delimited line for file storage. */
    public abstract String toCsvLine();
}
