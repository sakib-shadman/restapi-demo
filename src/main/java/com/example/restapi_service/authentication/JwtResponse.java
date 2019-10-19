package com.example.restapi_service.authentication;

import com.example.restapi_service.model.User;

import java.io.Serializable;
public class JwtResponse implements Serializable {
    private static final long serialVersionUID = -8091879091924046844L;
    private final String jwttoken;
    private User user;
    public JwtResponse(String jwttoken,User user) {
        this.jwttoken = jwttoken;
        this.user = user;
    }
    public String getToken() {
        return this.jwttoken;
    }

    public User getUser() {
        return user;
    }
}