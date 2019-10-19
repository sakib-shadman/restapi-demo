package com.example.restapi_service.repository;

import com.example.restapi_service.model.Blog;
import com.example.restapi_service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository  extends JpaRepository<User,Integer> {

    User findUserByUserName(String username);
}
