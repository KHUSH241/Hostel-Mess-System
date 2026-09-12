package com.hms.model;

import java.time.LocalDateTime;
public class Feedback {
    private final String feedbackId;
    private final String complaintId;
    private final int rating; 
    private final String comment;
    private final LocalDateTime submittedAt;

    public Feedback(String feedbackId, String complaintId, int rating, String comment, LocalDateTime submittedAt) {
        this.feedbackId = feedbackId;
        this.complaintId = complaintId;
        this.rating = rating;
        this.comment = comment;
        this.submittedAt = submittedAt;
    }

    public String getFeedbackId() { return feedbackId; }
    public String getComplaintId() { return complaintId; }
    public int getRating() { return rating; }
    public String getComment() { return comment; }
    public LocalDateTime getSubmittedAt() { return submittedAt; }

    public String toCsvLine() {
        return String.join("|", feedbackId, complaintId, String.valueOf(rating),
                comment == null ? "" : comment.replace("|", "/"), submittedAt.toString());
    }
}
