// 📁 service/BlogHistoryService.java
package com.techyatra.blog_api.service;

import com.techyatra.blog_api.model.Blog;
import com.techyatra.blog_api.model.BlogHistory;
import com.techyatra.blog_api.model.Category;
import com.techyatra.blog_api.repository.BlogHistoryRepository;
import com.techyatra.blog_api.repository.BlogRepository;
import com.techyatra.blog_api.repository.CategoryRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public class BlogHistoryService {

    private final BlogHistoryRepository repo;
    private final CategoryRepository categoryRepo;
    private final BlogRepository blogRepo;

    @Autowired
    public BlogHistoryService(BlogHistoryRepository repo, CategoryRepository categoryRepo, BlogRepository blogRepo) {
        this.repo = repo;
        this.categoryRepo = categoryRepo;
        this.blogRepo = blogRepo;
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

    public List<BlogHistory> getBlogByCategory(String email, UUID categoryId) {
        List<BlogHistory> histories = new ArrayList<>();
        Optional<Category> category = categoryRepo.findById(categoryId);
        if(category.isEmpty()) {
            throw new IllegalArgumentException("Category not found with ID: " + categoryId);
        }
        List<Blog> blogs = blogRepo.findBlogByCategoryId(category.get().getId());
        if (blogs.isEmpty()) {
            return new ArrayList<>();
        }
        for (Blog blog : blogs) {
            BlogHistory history = repo.findByUserEmailAndBlogId(email, blog.getId());
            if (history != null) {
                histories.add(history);
            }
        }
          
        return histories;
    }

    public List<BlogHistory> getByCategoryIdAndDates(String email, LocalDate fromDate, LocalDate toDate, UUID categoryId) {
        List<BlogHistory> histories = new ArrayList<>();
        Optional<Category> category = categoryRepo.findById(categoryId);
        if (category.isEmpty()) {
            throw new IllegalArgumentException("Category not found with ID: " + categoryId);
        }
        List<Blog> blogs = blogRepo.findBlogByCategoryId(category.get().getId());
        if (blogs.isEmpty()) {  
            return new ArrayList<>();
        }

        if (fromDate == null && toDate == null && categoryId == null && categoryId == null && email != null) {
            throw new IllegalArgumentException("At least one filter must be provided.");
        }

        for(Blog blog : blogs) {
            List<BlogHistory> blogHistories = repo.getDataBetweenDatesAndCategoryAndEmail(fromDate, toDate, blog.getId(), email);
            if (blogHistories != null && !blogHistories.isEmpty()) {
                histories.addAll(blogHistories);
            }
        }

        return histories;
    }
}
