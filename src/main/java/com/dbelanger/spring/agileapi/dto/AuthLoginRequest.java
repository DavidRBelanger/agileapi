package com.dbelanger.spring.agileapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class AuthLoginRequest {
    
    @NotBlank(message = "Email is required")
    @Email(message = "Must be a valid email address")
    public String email;
    
    @NotBlank(message = "Password is required")
    public String password;
}
