// 📁 service/BlogHistoryService.java
package com.techyatra.blog_api.service;

import com.techyatra.blog_api.model.BlogHistory;
import com.techyatra.blog_api.repository.BlogHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
        return existing;
    }

    public List<BlogHistory> getHistory(String email) {
        return repo.findByUserEmail(email);
    }

    public void markAsPaid(String email, String title) {
        BlogHistory history = repo.findByUserEmailAndBlogTitle(email, title);
        if (history != null) {
            history.setIsPaid(true);
            repo.save(history);
        }
    }
}
