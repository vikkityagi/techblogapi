package com.techyatra.blog_api.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.techyatra.blog_api.model.Blog;
import com.techyatra.blog_api.model.BlogHistory;

public interface BlogRepository extends JpaRepository<Blog, UUID> {
        void deleteByTitle(String title);

        List<Blog> findByDate(LocalDateTime date);

        List<Blog> findByUserEmail(String email);

        Blog findByTitle(String title);

        @Query("SELECT b FROM Blog b WHERE b.titleNumber = ?1")
        Blog findByTitleNumber(String titleNumber);

        @Query(value = "SELECT * FROM public.blog " +
                        "WHERE date >= :fromDate " +
                        "AND date < (:toDate)::date + 1", nativeQuery = true)
        List<Blog> getDataBetweenDates(@Param("fromDate") LocalDate fromDate,
                        @Param("toDate") LocalDate toDate);

        @Query("SELECT b FROM Blog b WHERE b.date = (SELECT MAX(b2.date) FROM Blog b2)")
        Blog findLatestEntry();

        @Query(value = "SELECT * FROM public.blog " +
                        "WHERE date >= :fromDate " +
                        "AND date < (:toDate)::date + 1 " +
                        "AND category_id = :categoryId", nativeQuery = true)
        List<Blog> getDataBetweenDatesAndCategory(@Param("fromDate") LocalDate fromDate,
                        @Param("toDate") LocalDate toDate, @Param("categoryId") UUID categoryId);

        @Query(value = "SELECT * FROM public.blog " +
                        "WHERE date >= :fromDate " +
                        "AND date < (:toDate)::date + 1 AND category_id = :categoryId", nativeQuery = true)
        List<Blog> findByCategoryIdBetweenDates(@Param("fromDate") LocalDate fromDate,
                        @Param("toDate") LocalDate toDate, @Param("categoryId") UUID categoryId);

        @Query("SELECT b FROM Blog b WHERE b.category = :categoryId")
        List<Blog> findBlogByCategoryId(@Param("categoryId") UUID categoryId);

        @Query(value = "SELECT * FROM public.blog " +
                        "WHERE date >= :fromDate " +
                        "AND date < (:toDate)::date + 1 AND category_id = :categoryId AND email = :email", nativeQuery = true)
        List<BlogHistory> getDataBetweenDatesAndCategoryAndEmail(LocalDate fromDate, LocalDate toDate, UUID categoryId,
                String email);

        // List<Blog> findAllBlogByDate();
}
