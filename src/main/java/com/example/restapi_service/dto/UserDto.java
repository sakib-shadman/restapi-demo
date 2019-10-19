package com.example.restapi_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    String accessToken;
    String userName;
    String userEmail;
    String userGender;
    String userAddress;
}
