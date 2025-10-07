package com.dbelanger.spring.agileapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class AuthRegisterRequest {
    
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    public String name;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Must be a valid email address")
    public String email;
    
    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 100, message = "Password must be at least 6 characters")
    public String password;
    
    @NotBlank(message = "Organization name is required")
    @Size(min = 2, max = 100, message = "Organization name must be between 2 and 100 characters")
    public String organizationName;
    
    @NotBlank(message = "Organization slug is required")
    @Pattern(regexp = "^[a-z0-9-]+$", message = "Slug must contain only lowercase letters, numbers, and hyphens")
    @Size(min = 2, max = 50, message = "Slug must be between 2 and 50 characters")
    public String organizationSlug;
}
