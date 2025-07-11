// 📁 controller/BlogHistoryController.java
package com.techyatra.blog_api.controller;

import com.techyatra.blog_api.model.Blog;
import com.techyatra.blog_api.model.BlogHistory;
import com.techyatra.blog_api.service.BlogHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

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
        BlogHistory existing = service.save(history);
        if(existing.getId() == null || existing.getId().equals(UUID.fromString("00000000-0000-0000-0000-000000000000"))) {
            // If the ID is null or a placeholder, it means the blog was not saved
            return new BlogHistory(); 
        } else {
            return existing; // Return the saved BlogHistory object
            
        }
    }

    @GetMapping("/{email}")
    public List<BlogHistory> getHistory(@PathVariable String email,@RequestParam(value = "from", required = false) LocalDate fromDate,
                                        @RequestParam(value = "to", required = false) LocalDate toDate) {
        return service.getHistory(email, fromDate, toDate);
    }

    @GetMapping("{id}/pay/{status}")
    public boolean markPaid(@PathVariable UUID id, @PathVariable Boolean status) {
        return service.markAsPaid(id, status);
    }
}
