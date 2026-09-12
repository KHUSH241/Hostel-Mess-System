package com.hms.service;

import com.hms.model.*;
import com.hms.util.FileStorageUtil;
import com.hms.util.IdGenerator;
import com.hms.util.InvalidInputException;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class ComplaintService {
    private static final String COMPLAINTS_FILE = "data/complaints.txt";

    public static final int ESCALATION_THRESHOLD_DAYS = 3;

    private final List<Complaint> complaints = new ArrayList<>();
    private final IdGenerator idGen = new IdGenerator("CMP", 0);
    private final NotificationLogger logger;

    public ComplaintService(NotificationLogger logger) {
        this.logger = logger;
        load();
    }

    private void load() {
        for (String line : FileStorageUtil.readLines(COMPLAINTS_FILE)) {
            String[] f = line.split("\\|", -1);
            try {
                Complaint c = new Complaint(f[0], f[1], f[2], Category.valueOf(f[3]), f[4],
                        Priority.valueOf(f[5]), LocalDateTime.parse(f[7]));
                c.setStatus(ComplaintStatus.valueOf(f[6]));
                if (!f[8].equals("-")) c.setResolvedAt(LocalDateTime.parse(f[8]));
                c.setEscalated(Boolean.parseBoolean(f[9]));
                if (f.length > 10) c.setAdminRemarks(f[10]);
                complaints.add(c);
            } catch (Exception e) {
                System.err.println("Skipping malformed complaint row: " + line);
            }
        }
    }

    private void persistAll() {
        List<String> lines = complaints.stream().map(Complaint::toCsvLine).collect(Collectors.toList());
        FileStorageUtil.overwriteAll(COMPLAINTS_FILE, lines);
    }

    public Complaint fileComplaint(String studentId, String studentName, Category category,
                                    String description, Priority priority) throws InvalidInputException {
        if (description == null || description.isBlank()) {
            throw new InvalidInputException("Complaint description cannot be empty.");
        }
        if (description.length() > 500) {
            throw new InvalidInputException("Description too long (max 500 characters).");
        }
        Complaint c = new Complaint(idGen.next(), studentId, studentName, category, description.trim(),
                priority, LocalDateTime.now());
        complaints.add(c);
        persistAll();
        return c;
    }

    public List<Complaint> getComplaintsForStudent(String studentId) {
        return complaints.stream()
                .filter(c -> c.getStudentId().equals(studentId))
                .sorted(Comparator.comparing(Complaint::getCreatedAt).reversed())
                .collect(Collectors.toList());
    }

    public List<Complaint> getAllComplaints() {
        return complaints.stream()
                .sorted(Comparator.comparing(Complaint::getCreatedAt).reversed())
                .collect(Collectors.toList());
    }

    public List<Complaint> getByStatus(ComplaintStatus status) {
        return complaints.stream().filter(c -> c.getStatus() == status).collect(Collectors.toList());
    }

    public Optional<Complaint> findById(String complaintId) {
        return complaints.stream().filter(c -> c.getComplaintId().equals(complaintId)).findFirst();
    }

    public void updateStatus(String complaintId, ComplaintStatus newStatus, String remarks) throws InvalidInputException {
        Complaint c = findById(complaintId)
                .orElseThrow(() -> new InvalidInputException("No complaint with ID " + complaintId));
        c.setStatus(newStatus);
        if (remarks != null && !remarks.isBlank()) {
            c.setAdminRemarks(remarks);
        }
        if (newStatus == ComplaintStatus.RESOLVED) {
            c.setResolvedAt(LocalDateTime.now());
            c.setEscalated(false);
        }
        persistAll();
    }


    public int runEscalationCheck() {
        LocalDateTime now = LocalDateTime.now();
        int escalatedCount = 0;
        for (Complaint c : complaints) {
            boolean stillOpen = c.getStatus() == ComplaintStatus.PENDING || c.getStatus() == ComplaintStatus.IN_PROGRESS;
            if (stillOpen && !c.isEscalated() && c.daysOpen(now) >= ESCALATION_THRESHOLD_DAYS) {
                c.setEscalated(true);
                c.setStatus(ComplaintStatus.ESCALATED);
                escalatedCount++;
                logger.logEscalation(c.getComplaintId(), c.getCategory().name(), c.daysOpen(now));
            }
        }
        if (escalatedCount > 0) {
            persistAll();
        }
        return escalatedCount;
    }
}
