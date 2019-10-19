package com.example.restapi_service.controller;


import com.example.restapi_service.model.Blog;
import com.example.restapi_service.model.BlogMockedData;
import com.example.restapi_service.repository.BlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class BlogController {


    private BlogMockedData blogMockedData = BlogMockedData.getInstance();

    @Autowired
    BlogRepository blogRepository;

    @GetMapping("/blog")
    public String index() {
        return "Hello from BlogController.java";
    }


    @GetMapping("/blog/{id}")
    public Blog show(@PathVariable String id) {
        int blogId = Integer.parseInt(id);
        return blogMockedData.getBlogById(blogId);
    }

    @PostMapping("/blog/search")
    public List<Blog> search(@RequestBody Map<String, String> body) {
        String searchTerm = body.get("text");
        return blogMockedData.searchBlogs(searchTerm);
    }

    @PostMapping("/blog")
    public Blog create(@RequestBody Map<String, String> body) {
        int id = Integer.parseInt(body.get("id"));
        String title = body.get("title");
        String content = body.get("content");
        return blogMockedData.createBlog(id, title, content);
    }

    @PutMapping("/blog/{id}")
    public Blog update(@PathVariable String id, @RequestBody Map<String, String> body) {
        int blogId = Integer.parseInt(id);
        String title = body.get("title");
        String content = body.get("content");
        return blogMockedData.updateBlog(blogId, title, content);
    }

    @DeleteMapping("blog/{id}")
    public boolean delete(@PathVariable String id) {
        int blogId = Integer.parseInt(id);
        return blogMockedData.delete(blogId);
    }


    @GetMapping("/blog/getAll")
    public List<Blog> getAllBlog() {

        return blogMockedData.fetchBlogs();
    }



    @PostMapping("/blog/createAndSave")
    public Blog createAndSave(@RequestBody Map<String, String> body) {
        String title = body.get("title");
        String content = body.get("content");
        return blogRepository.save(new Blog(title, content));
    }

    @GetMapping("/blog/getAllFromDB")
    public  List<Blog> getAllBlogFromDB() {

        return blogRepository.findAll();
    }


    @DeleteMapping("blog/deleteBlog/{id}")
    public void deleteFromDB(@PathVariable String id) {
        int blogId = Integer.parseInt(id);
        blogRepository.deleteById(blogId);
    }


    @GetMapping("/blog/getAllFromDB/{title}")
    public List<Blog> getAllBlogByTitle(@PathVariable String title) {
        return blogRepository.findAllByTitleContaining(title);
    }
}
