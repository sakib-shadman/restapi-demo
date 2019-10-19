package com.example.restapi_service.dto;

public enum StatusCode {


    PROCESSING(1, "PROCESSING"),
    SUCCESS(2,"SUCCESS"),
    FAILED(3,"FAILED");

    private final Integer key;
    private final String value;

    StatusCode(Integer key, String value) {
        this.key = key;
        this.value = value;
    }

    public Integer getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
