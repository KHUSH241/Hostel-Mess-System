package com.hms.service;

import com.hms.model.Complaint;
import com.hms.model.ComplaintStatus;
import com.hms.model.Feedback;
import com.hms.util.FileStorageUtil;
import com.hms.util.IdGenerator;
import com.hms.util.InvalidInputException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

public class FeedbackService {
    private static final String FEEDBACK_FILE = "data/feedback.txt";

    private final List<Feedback> feedbackList = new ArrayList<>();
    private final IdGenerator idGen = new IdGenerator("FB", 0);

    public FeedbackService() {
        for (String line : FileStorageUtil.readLines(FEEDBACK_FILE)) {
            String[] f = line.split("\\|", -1);
            try {
                feedbackList.add(new Feedback(f[0], f[1], Integer.parseInt(f[2]), f[3], LocalDateTime.parse(f[4])));
            } catch (Exception e) {
                System.err.println("Skipping malformed feedback row: " + line);
            }
        }
    }

    public Feedback submitFeedback(Complaint complaint, int rating, String comment) throws InvalidInputException {
        if (complaint.getStatus() != ComplaintStatus.RESOLVED) {
            throw new InvalidInputException("Feedback can only be left once a complaint is RESOLVED.");
        }
        if (rating < 1 || rating > 5) {
            throw new InvalidInputException("Rating must be between 1 and 5.");
        }
        Feedback fb = new Feedback(idGen.next(), complaint.getComplaintId(), rating, comment, LocalDateTime.now());
        feedbackList.add(fb);
        FileStorageUtil.appendLine(FEEDBACK_FILE, fb.toCsvLine());
        return fb;
    }

    public OptionalDouble averageRating() {
        return feedbackList.stream().mapToInt(Feedback::getRating).average();
    }

    public List<Feedback> all() {
        return feedbackList;
    }
}
