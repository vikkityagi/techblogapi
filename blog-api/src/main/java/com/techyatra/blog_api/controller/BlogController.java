package com.techyatra.blog_api.controller;

import com.techyatra.blog_api.model.Blog;
import com.techyatra.blog_api.service.BlogService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/blogs")
@CrossOrigin(origins = "*")
public class BlogController {

    private final BlogService service;

    public BlogController(BlogService service) {
        this.service = service;
    }

    @PostMapping
    public Blog createBlog(@RequestBody Blog blog) {
        return service.save(blog);
    }

    @GetMapping
    public List<Blog> getAllBlogs() {
        return service.getAll();
    }

    @GetMapping("/today")
    public List<Blog> getTodayBlogs(@RequestParam LocalDateTime date) {
        return service.getByDate(date);
    }

    @GetMapping("/user")
    public List<Blog> getUserBlogs(@RequestParam String email) {
        return service.getByUser(email);
    }

    @DeleteMapping("/delete")
    public void deleteByTitle(@RequestParam String title) {
        service.deleteByTitle(title);
    }

    @GetMapping("/search")
    public Blog searchByTitle(@RequestParam String title) {
        return service.getByTitle(title);
    }

    @GetMapping("/{id}")
    public Blog getBlogById(@PathVariable UUID id) {
        return service.getById(id);
    }
}
