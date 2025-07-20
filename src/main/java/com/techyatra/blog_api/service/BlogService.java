package com.techyatra.blog_api.service;

import com.techyatra.blog_api.model.Blog;
import com.techyatra.blog_api.repository.BlogRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class BlogService {

    private final BlogRepository repo;

    public BlogService(BlogRepository repo) {
        this.repo = repo;
    }

    public Blog save(Blog blog) {
        Blog existingBlog = repo.findByTitleNumber(blog.getTitleNumber());
        if (existingBlog != null) {
            throw new IllegalArgumentException("Blog title number already exists.");
        }
        Blog existingBlogWithSameTitle = repo.findByTitle(blog.getTitle());
        if (existingBlogWithSameTitle != null) {
            throw new IllegalArgumentException("Blog title already exists.");
        }
        return repo.save(blog);
    }

    public List<Blog> getAll(LocalDate fromDate, LocalDate toDate) {
        if (fromDate != null && toDate != null) {
            if (fromDate.isAfter(toDate)) {
                throw new IllegalArgumentException("From date cannot be after to date.");
            }
            System.out.println("Fetching blogs between dates: " + fromDate + " and " + toDate);
            return repo.getDataBetweenDates(fromDate, toDate);
        } else {
            LocalDate now = LocalDate.now();
            return repo.getDataBetweenDates(now.minusDays(7), now);
        }
    }

    public void deleteByTitle(String title) {
        repo.deleteByTitle(title);
    }

    public List<Blog> getByDate(LocalDateTime date) {
        return repo.findByDate(date);
    }

    public List<Blog> getByUser(String email) {
        return repo.findByUserEmail(email);
    }

    public Blog getByTitle(String title) {
        return repo.findByTitle(title);
    }

    public Blog getById(UUID id) {
        return repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Blog not found with id: " + id));
    }

    public Blog getLatestBlog() {
        return repo.findLatestEntry();
    }
}
