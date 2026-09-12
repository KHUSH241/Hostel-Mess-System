package com.hms.ui;

import com.hms.model.Student;
import com.hms.model.User;
import com.hms.service.AuthService;
import com.hms.util.InvalidInputException;

import java.util.Scanner;

public class AuthMenu {
    private final Scanner sc;
    private final AuthService authService;

    public AuthMenu(Scanner sc, AuthService authService) {
        this.sc = sc;
        this.authService = authService;
    }
    public User show() {
        while (true) {
            System.out.println("\n===== Hostel & Mess Complaint System =====");
            System.out.println("1. Login");
            System.out.println("2. Register as Student");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            String choice = sc.nextLine().trim();
            switch (choice) {
                case "1": {
                    User u = login();
                    if (u != null) return u;
                    break;
                }
                case "2": register(); break;
                case "3": return null;
                default: System.out.println("Invalid option, try again.");
            }
        }
    }

    private User login() {
        System.out.print("Username: ");
        String username = sc.nextLine().trim();
        System.out.print("Password: ");
        String password = sc.nextLine();
        User u = authService.login(username, password);
        if (u == null) {
            System.out.println("Invalid credentials.");
        } else {
            System.out.println("Welcome, " + u.getFullName() + " (" + u.getRole() + ")");
        }
        return u;
    }

    private void register() {
        try {
            System.out.print("Choose a username: ");
            String username = sc.nextLine().trim();
            System.out.print("Choose a password (min 4 chars): ");
            String password = sc.nextLine();
            System.out.print("Full name: ");
            String fullName = sc.nextLine().trim();
            System.out.print("Hostel block (e.g. B3): ");
            String block = sc.nextLine().trim();
            System.out.print("Room number: ");
            String room = sc.nextLine().trim();

            Student s = authService.registerStudent(username, password, fullName, block, room);
            System.out.println("Registered successfully. Your student ID is " + s.getUserId() + " - you can log in now.");
        } catch (InvalidInputException e) {
            System.out.println("Registration failed: " + e.getMessage());
        }
    }
}
