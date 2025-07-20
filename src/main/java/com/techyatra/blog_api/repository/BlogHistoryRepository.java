package com.techyatra.blog_api.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.techyatra.blog_api.model.BlogHistory;

public interface BlogHistoryRepository extends JpaRepository<BlogHistory, UUID> {

    @Query("SELECT b FROM BlogHistory b WHERE b.userEmail = :userEmail AND b.createdAt >= :sevenDaysAgo ORDER BY b.createdAt DESC")
    List<BlogHistory> findRecentBlogsByUserEmail(@Param("userEmail") String userEmail,
            @Param("sevenDaysAgo") LocalDateTime sevenDaysAgo);

    @Query("SELECT bh FROM BlogHistory bh JOIN bh.blog b WHERE bh.userEmail = :userEmail AND b.title = :title")
    BlogHistory findByUserEmailAndBlogTitle(String userEmail, String title);

    @Query(value = "SELECT * FROM public.blog_history " +
            "WHERE created_at >= :fromDate " +
            "AND created_at < (:toDate)::date + 1 and user_email=:email", nativeQuery = true)
    List<BlogHistory> findByUserEmailAndCreatedAtBetween(String email, LocalDate fromDate, LocalDate toDate);
}
