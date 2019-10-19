package com.example.restapi_service.repository;

import com.example.restapi_service.model.Blog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BlogRepository extends JpaRepository<Blog,Integer> {


    List<Blog> findAllByTitleContaining(String title);
}
