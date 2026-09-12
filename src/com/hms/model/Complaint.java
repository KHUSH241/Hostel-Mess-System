package com.hms.model;

import java.time.LocalDateTime;

public class Complaint {
    private final String complaintId;
    private final String studentId;
    private final String studentName;
    private final Category category;
    private final String description;
    private final Priority priority;
    private ComplaintStatus status;
    private final LocalDateTime createdAt;
    private LocalDateTime resolvedAt; 
    private boolean escalated;
    private String adminRemarks;

    public Complaint(String complaintId, String studentId, String studentName, Category category,
                      String description, Priority priority, LocalDateTime createdAt) {
        this.complaintId = complaintId;
        this.studentId = studentId;
        this.studentName = studentName;
        this.category = category;
        this.description = description;
        this.priority = priority;
        this.status = ComplaintStatus.PENDING;
        this.createdAt = createdAt;
        this.escalated = false;
        this.adminRemarks = "";
    }

    public String getComplaintId() { return complaintId; }
    public String getStudentId() { return studentId; }
    public String getStudentName() { return studentName; }
    public Category getCategory() { return category; }
    public String getDescription() { return description; }
    public Priority getPriority() { return priority; }
    public ComplaintStatus getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getResolvedAt() { return resolvedAt; }
    public boolean isEscalated() { return escalated; }
    public String getAdminRemarks() { return adminRemarks; }

    public void setStatus(ComplaintStatus status) { this.status = status; }
    public void setResolvedAt(LocalDateTime resolvedAt) { this.resolvedAt = resolvedAt; }
    public void setEscalated(boolean escalated) { this.escalated = escalated; }
    public void setAdminRemarks(String adminRemarks) { this.adminRemarks = adminRemarks; }

    public long daysOpen(LocalDateTime now) {
        LocalDateTime end = (resolvedAt != null) ? resolvedAt : now;
        return java.time.Duration.between(createdAt, end).toDays();
    }

    public String toCsvLine() {
        return String.join("|",
                complaintId, studentId, escape(studentName), category.name(), escape(description),
                priority.name(), status.name(), createdAt.toString(),
                resolvedAt == null ? "-" : resolvedAt.toString(),
                String.valueOf(escalated), escape(adminRemarks));
    }

    private String escape(String s) {
        return s == null ? "" : s.replace("|", "/").replace("\n", " ");
    }

    @Override
    public String toString() {
        return String.format("[%s] %-22s | %-9s | %-11s | opened %s | %s",
                complaintId, category, priority, status, createdAt.toLocalDate(),
                escalated ? "(ESCALATED)" : "");
    }
}
