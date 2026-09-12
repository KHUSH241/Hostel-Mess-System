package com.hms.service;

import com.hms.util.FileStorageUtil;

import java.time.LocalDateTime;

/**
 * Very small logging module - every escalation event is appended to
 * data/escalation.log so there's an audit trail (satisfies the "logging /
 * monitoring" non-functional requirement).
 */
public class NotificationLogger {
    private static final String LOG_FILE = "data/escalation.log";

    public void logEscalation(String complaintId, String category, long daysOpen) {
        String entry = String.format("[%s] Complaint %s (%s) auto-escalated after %d day(s) unresolved.",
                LocalDateTime.now(), complaintId, category, daysOpen);
        FileStorageUtil.appendLine(LOG_FILE, entry);
        System.out.println(">> ESCALATION: " + entry);
    }

    public void logInfo(String message) {
        String entry = String.format("[%s] %s", LocalDateTime.now(), message);
        FileStorageUtil.appendLine(LOG_FILE, entry);
    }
}
