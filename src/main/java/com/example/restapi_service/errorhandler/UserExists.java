package com.example.restapi_service.errorhandler;

public class UserExists extends Exception {
    public UserExists(String message) {
        super(message);
    }
}
