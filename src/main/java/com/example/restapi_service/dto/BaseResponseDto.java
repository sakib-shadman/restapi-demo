package com.example.restapi_service.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponseDto {

    private String status;
    private StatusCode statusCode;
    private String message;
    private HashMap<String, List<String>> errors;
    private List<String> allErrors;
    private String entityId;
}
