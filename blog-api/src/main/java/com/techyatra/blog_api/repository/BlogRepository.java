package com.techyatra.blog_api.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.techyatra.blog_api.model.Blog;

public interface BlogRepository extends JpaRepository<Blog, UUID> {
    void deleteByTitle(String title);

    List<Blog> findByDate(LocalDateTime date);

    List<Blog> findByUserEmail(String email);

    Blog findByTitle(String title);

    @Query("SELECT b FROM Blog b WHERE b.titleNumber = ?1")
    Blog findByTitleNumber(String titleNumber);
}
