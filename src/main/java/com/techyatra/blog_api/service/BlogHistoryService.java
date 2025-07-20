// 📁 service/BlogHistoryService.java
package com.techyatra.blog_api.service;

import com.techyatra.blog_api.model.BlogHistory;
import com.techyatra.blog_api.repository.BlogHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class BlogHistoryService {

    private final BlogHistoryRepository repo;

    @Autowired
    public BlogHistoryService(BlogHistoryRepository repo) {
        this.repo = repo;
    }

    public BlogHistory save(BlogHistory history) {
        BlogHistory existing = repo.findByUserEmailAndBlogTitle(history.getUserEmail(), history.getBlog().getTitle());
        if (existing == null) {
            return repo.save(history);
        }
        return history;
    }

    public List<BlogHistory> getHistory(String email, LocalDate fromDate, LocalDate toDate) {
        if(fromDate != null && toDate != null) {

            if (fromDate.isAfter(toDate)) {
                throw new IllegalArgumentException("From date cannot be after to date.");
            }
            return repo.findByUserEmailAndCreatedAtBetween(email, fromDate, toDate);
        }
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);
        return repo.findRecentBlogsByUserEmail(email, sevenDaysAgo);
    }

    public boolean markAsPaid(UUID id, Boolean status) {
        BlogHistory history = repo.findById(id).orElse(null);
        if (history != null) {
            history.setIsPaid(status);
            repo.save(history);
            return true;
        }
        return false;
    }
}
