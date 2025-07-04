// 📁 controller/BlogHistoryController.java
package com.techyatra.blog_api.controller;

import com.techyatra.blog_api.model.BlogHistory;
import com.techyatra.blog_api.service.BlogHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/history")
@CrossOrigin(origins = "*")
public class BlogHistoryController {

    private final BlogHistoryService service;

    public BlogHistoryController(BlogHistoryService service) {
        this.service = service;
    }

    @PostMapping("/save")
    public BlogHistory save(@RequestBody BlogHistory history) {
        return service.save(history);
    }

    @GetMapping("/{email}")
    public List<BlogHistory> getHistory(@PathVariable String email) {
        return service.getHistory(email);
    }

    @PostMapping("/mark-paid")
    public void markPaid(@RequestParam String email, @RequestParam String title) {
        service.markAsPaid(email, title);
    }
}
