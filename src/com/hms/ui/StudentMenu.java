package com.hms.ui;

import com.hms.model.*;
import com.hms.service.ComplaintService;
import com.hms.service.FeedbackService;
import com.hms.util.InvalidInputException;

import java.util.List;
import java.util.Scanner;

public class StudentMenu {
    private final Scanner sc;
    private final ComplaintService complaintService;
    private final FeedbackService feedbackService;

    public StudentMenu(Scanner sc, ComplaintService complaintService, FeedbackService feedbackService) {
        this.sc = sc;
        this.complaintService = complaintService;
        this.feedbackService = feedbackService;
    }

    public void show(Student student) {
        boolean running = true;
        while (running) {
            System.out.println("\n--- Student Menu (" + student.getFullName() + ", Room " + student.getRoomNumber() + ") ---");
            System.out.println("1. File a new complaint");
            System.out.println("2. View my complaints");
            System.out.println("3. Leave feedback on a resolved complaint");
            System.out.println("4. Logout");
            System.out.print("Choose an option: ");
            String choice = sc.nextLine().trim();
            switch (choice) {
                case "1": fileComplaint(student); break;
                case "2": viewMyComplaints(student); break;
                case "3": leaveFeedback(student); break;
                case "4": running = false; break;
                default: System.out.println("Invalid option, try again.");
            }
        }
    }

    private void fileComplaint(Student student) {
        try {
            System.out.println("\nCategories:");
            Category[] cats = Category.values();
            for (int i = 0; i < cats.length; i++) System.out.printf("  %d. %s%n", i + 1, cats[i]);
            System.out.print("Choose category number: ");
            int ci = Integer.parseInt(sc.nextLine().trim()) - 1;
            Category category = cats[ci];

            System.out.print("Priority (1=LOW, 2=MEDIUM, 3=HIGH): ");
            int pi = Integer.parseInt(sc.nextLine().trim());
            Priority priority = pi == 3 ? Priority.HIGH : pi == 2 ? Priority.MEDIUM : Priority.LOW;

            System.out.print("Describe the issue: ");
            String desc = sc.nextLine();

            Complaint c = complaintService.fileComplaint(student.getUserId(), student.getFullName(),
                    category, desc, priority);
            System.out.println("Complaint filed successfully with ID: " + c.getComplaintId());
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Invalid category selection.");
        } catch (InvalidInputException e) {
            System.out.println("Could not file complaint: " + e.getMessage());
        }
    }

    private void viewMyComplaints(Student student) {
        List<Complaint> mine = complaintService.getComplaintsForStudent(student.getUserId());
        if (mine.isEmpty()) {
            System.out.println("You haven't filed any complaints yet.");
            return;
        }
        System.out.println("\nYour complaints:");
        for (Complaint c : mine) {
            System.out.println("  " + c);
            if (!c.getAdminRemarks().isBlank()) {
                System.out.println("      admin remarks: " + c.getAdminRemarks());
            }
        }
    }

    private void leaveFeedback(Student student) {
        List<Complaint> resolved = complaintService.getComplaintsForStudent(student.getUserId()).stream()
                .filter(c -> c.getStatus() == ComplaintStatus.RESOLVED).toList();
        if (resolved.isEmpty()) {
            System.out.println("You have no resolved complaints to give feedback on yet.");
            return;
        }
        System.out.println("Resolved complaints:");
        for (Complaint c : resolved) System.out.println("  " + c);
        System.out.print("Enter complaint ID to give feedback on: ");
        String id = sc.nextLine().trim();
        Complaint target = resolved.stream().filter(c -> c.getComplaintId().equalsIgnoreCase(id)).findFirst().orElse(null);
        if (target == null) {
            System.out.println("That ID isn't in your resolved list.");
            return;
        }
        try {
            System.out.print("Rating (1-5): ");
            int rating = Integer.parseInt(sc.nextLine().trim());
            System.out.print("Comment (optional): ");
            String comment = sc.nextLine();
            feedbackService.submitFeedback(target, rating, comment);
            System.out.println("Thanks! Feedback recorded.");
        } catch (NumberFormatException e) {
            System.out.println("Rating must be a number 1-5.");
        } catch (InvalidInputException e) {
            System.out.println("Could not submit feedback: " + e.getMessage());
        }
    }
}
