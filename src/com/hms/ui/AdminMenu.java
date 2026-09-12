package com.hms.ui;

import com.hms.model.Admin;
import com.hms.model.Complaint;
import com.hms.model.ComplaintStatus;
import com.hms.service.ComplaintService;
import com.hms.service.ReportService;
import com.hms.util.InvalidInputException;

import java.util.List;
import java.util.Scanner;

public class AdminMenu {
    private final Scanner sc;
    private final ComplaintService complaintService;
    private final ReportService reportService;

    public AdminMenu(Scanner sc, ComplaintService complaintService, ReportService reportService) {
        this.sc = sc;
        this.complaintService = complaintService;
        this.reportService = reportService;
    }

    public void show(Admin admin) {
        int escalated = complaintService.runEscalationCheck();
        if (escalated > 0) {
            System.out.println(escalated + " complaint(s) were auto-escalated since last check.");
        }
        boolean running = true;
        while (running) {
            System.out.println("\n--- Admin Dashboard (" + admin.getFullName() + ") ---");
            System.out.println("1. View all complaints");
            System.out.println("2. View complaints by status");
            System.out.println("3. Update complaint status");
            System.out.println("4. Run escalation check now (threshold: "
                    + ComplaintService.ESCALATION_THRESHOLD_DAYS + " days)");
            System.out.println("5. View analytics summary");
            System.out.println("6. Logout");
            System.out.print("Choose an option: ");
            String choice = sc.nextLine().trim();
            switch (choice) {
                case "1": printList(complaintService.getAllComplaints()); break;
                case "2": viewByStatus(); break;
                case "3": updateStatus(); break;
                case "4": {
                    int n = complaintService.runEscalationCheck();
                    System.out.println(n == 0 ? "No complaints needed escalation." : n + " complaint(s) escalated.");
                    break;
                }
                case "5": reportService.printSummary(complaintService.getAllComplaints()); break;
                case "6": running = false; break;
                default: System.out.println("Invalid option, try again.");
            }
        }
    }

    private void printList(List<Complaint> list) {
        if (list.isEmpty()) {
            System.out.println("(none)");
            return;
        }
        for (Complaint c : list) {
            System.out.println("  " + c + " | by " + c.getStudentName());
        }
    }

    private void viewByStatus() {
        System.out.print("Status (PENDING / IN_PROGRESS / RESOLVED / ESCALATED): ");
        try {
            ComplaintStatus status = ComplaintStatus.valueOf(sc.nextLine().trim().toUpperCase());
            printList(complaintService.getByStatus(status));
        } catch (IllegalArgumentException e) {
            System.out.println("Not a valid status.");
        }
    }

    private void updateStatus() {
        System.out.print("Complaint ID: ");
        String id = sc.nextLine().trim();
        System.out.print("New status (PENDING / IN_PROGRESS / RESOLVED): ");
        String statusStr = sc.nextLine().trim().toUpperCase();
        System.out.print("Remarks (optional): ");
        String remarks = sc.nextLine();
        try {
            ComplaintStatus status = ComplaintStatus.valueOf(statusStr);
            complaintService.updateStatus(id, status, remarks);
            System.out.println("Updated.");
        } catch (IllegalArgumentException e) {
            System.out.println("Not a valid status.");
        } catch (InvalidInputException e) {
            System.out.println("Update failed: " + e.getMessage());
        }
    }
}
