package com.hms.service;

import com.hms.model.*;
import com.hms.util.FileStorageUtil;
import com.hms.util.IdGenerator;
import com.hms.util.InvalidInputException;

import java.util.ArrayList;
import java.util.List;

public class AuthService {
    private static final String USERS_FILE = "data/users.txt";

    private final List<User> users = new ArrayList<>();
    private final IdGenerator studentIdGen = new IdGenerator("STU", 0);
    private final IdGenerator adminIdGen = new IdGenerator("ADM", 0);

    public AuthService() {
        load();
        seedDefaultAdminIfEmpty();
    }

    private void load() {
        for (String line : FileStorageUtil.readLines(USERS_FILE)) {
            String[] f = line.split("\\|", -1);
            try {
                if (f[0].equals("STUDENT")) {
                    users.add(new Student(f[1], f[2], f[3], f[4], f[5], f[6]));
                } else if (f[0].equals("ADMIN")) {
                    users.add(new Admin(f[1], f[2], f[3], f[4], f[5]));
                }
            } catch (Exception e) {
                System.err.println("Skipping malformed user row: " + line);
            }
        }
    }

    private void seedDefaultAdminIfEmpty() {
        boolean hasAdmin = users.stream().anyMatch(u -> u.getRole().equals("ADMIN"));
        if (!hasAdmin) {
            Admin admin = new Admin(adminIdGen.next(), "admin", "admin123", "Chief Warden", "Hostel Warden");
            users.add(admin);
            FileStorageUtil.appendLine(USERS_FILE, admin.toCsvLine());
            System.out.println("[setup] No admin account found - created default admin (username: admin / password: admin123). Change this after first login in a real deployment.");
        }
    }

    public Student registerStudent(String username, String password, String fullName,
                                    String hostelBlock, String roomNumber) throws InvalidInputException {
        validateUsernameFree(username);
        if (password == null || password.length() < 4) {
            throw new InvalidInputException("Password must be at least 4 characters.");
        }
        Student s = new Student(studentIdGen.next(), username, password, fullName, hostelBlock, roomNumber);
        users.add(s);
        FileStorageUtil.appendLine(USERS_FILE, s.toCsvLine());
        return s;
    }

    private void validateUsernameFree(String username) throws InvalidInputException {
        if (username == null || username.isBlank()) {
            throw new InvalidInputException("Username cannot be empty.");
        }
        for (User u : users) {
            if (u.getUsername().equalsIgnoreCase(username)) {
                throw new InvalidInputException("Username already taken.");
            }
        }
    }

    public User login(String username, String password) {
        for (User u : users) {
            if (u.getUsername().equalsIgnoreCase(username) && u.checkPassword(password)) {
                return u;
            }
        }
        return null;
    }
}
