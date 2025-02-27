package com.project.security.jwt.model;

import lombok.Data;

@Data
public class TokenRefreshRequest {
    private String refreshToken;

    // Getters and Setters
}
