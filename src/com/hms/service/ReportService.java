package com.hms.service;

import com.hms.model.Category;
import com.hms.model.Complaint;
import com.hms.model.ComplaintStatus;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class ReportService {

    public Map<ComplaintStatus, Long> countByStatus(List<Complaint> complaints) {
        return complaints.stream()
                .collect(Collectors.groupingBy(Complaint::getStatus, LinkedHashMap::new, Collectors.counting()));
    }

    public Map<Category, Long> countByCategory(List<Complaint> complaints) {
        return complaints.stream()
                .collect(Collectors.groupingBy(Complaint::getCategory, LinkedHashMap::new, Collectors.counting()));
    }

    public double averageResolutionHours(List<Complaint> complaints) {
        List<Complaint> resolved = complaints.stream()
                .filter(c -> c.getStatus() == ComplaintStatus.RESOLVED && c.getResolvedAt() != null)
                .collect(Collectors.toList());
        if (resolved.isEmpty()) return 0.0;
        double totalHours = 0;
        for (Complaint c : resolved) {
            totalHours += ChronoUnit.MINUTES.between(c.getCreatedAt(), c.getResolvedAt()) / 60.0;
        }
        return totalHours / resolved.size();
    }

    public long escalatedCount(List<Complaint> complaints) {
        return complaints.stream().filter(Complaint::isEscalated).count();
    }

    public void printSummary(List<Complaint> complaints) {
        System.out.println("\n===== ADMIN ANALYTICS SUMMARY =====");
        System.out.println("Total complaints: " + complaints.size());
        countByStatus(complaints).forEach((k, v) -> System.out.printf("  %-12s : %d%n", k, v));
        System.out.println("\nBy category:");
        countByCategory(complaints).forEach((k, v) -> System.out.printf("  %-24s : %d%n", k, v));
        System.out.printf("%nAverage resolution time : %.1f hours (based on %d resolved complaints)%n",
                averageResolutionHours(complaints),
                complaints.stream().filter(c -> c.getStatus() == ComplaintStatus.RESOLVED).count());
        System.out.println("Currently escalated     : " + escalatedCount(complaints));
        System.out.println("====================================\n");
    }
}
