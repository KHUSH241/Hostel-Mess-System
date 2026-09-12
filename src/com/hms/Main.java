package com.hms;

import com.hms.model.Admin;
import com.hms.model.Student;
import com.hms.model.User;
import com.hms.service.*;
import com.hms.ui.AdminMenu;
import com.hms.ui.AuthMenu;
import com.hms.ui.StudentMenu;

import java.util.Scanner;

/**
 * Hostel & Mess Complaint and Feedback System
 * Entry point - wires up the service layer and runs the console loop.
 */
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        NotificationLogger logger = new NotificationLogger();
        AuthService authService = new AuthService();
        ComplaintService complaintService = new ComplaintService(logger);
        FeedbackService feedbackService = new FeedbackService();
        ReportService reportService = new ReportService();

        AuthMenu authMenu = new AuthMenu(sc, authService);
        StudentMenu studentMenu = new StudentMenu(sc, complaintService, feedbackService);
        AdminMenu adminMenu = new AdminMenu(sc, complaintService, reportService);

        System.out.println("Starting Hostel & Mess Complaint and Feedback System...");

        while (true) {
            User user = authMenu.show();
            if (user == null) {
                System.out.println("Goodbye!");
                break;
            }
            if (user instanceof Student student) {
                studentMenu.show(student);
            } else if (user instanceof Admin admin) {
                adminMenu.show(admin);
            }
        }
        sc.close();
    }
}
