package com.techyatra.blog_api.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.techyatra.blog_api.model.BlogHistory;

public interface BlogHistoryRepository extends JpaRepository<BlogHistory, UUID> {
    List<BlogHistory> findByUserEmail(String userEmail);

    @Query("SELECT bh FROM BlogHistory bh JOIN bh.blog b WHERE bh.userEmail = :userEmail AND b.title = :title")
    BlogHistory findByUserEmailAndBlogTitle(String userEmail, String title);
}
