package com.dbelanger.spring.agileapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ProjectRequest {
    
    @NotBlank(message = "Project name is required")
    @Size(min = 3, max = 100, message = "Project name must be between 3 and 100 characters")
    public String name;
    
    @Size(max = 500, message = "Description must not exceed 500 characters")
    public String description;
}
