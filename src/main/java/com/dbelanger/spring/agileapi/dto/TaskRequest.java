package com.dbelanger.spring.agileapi.dto;

import com.dbelanger.spring.agileapi.model.Task;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class TaskRequest {
    
    @NotBlank(message = "Task title is required")
    @Size(min = 3, max = 200, message = "Task title must be between 3 and 200 characters")
    public String title;
    
    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    public String description;
    
    public Task.Status status;
    
    @NotNull(message = "Priority is required")
    @Min(value = 1, message = "Priority must be between 1 and 5")
    @Max(value = 5, message = "Priority must be between 1 and 5")
    public Integer priority;
    
    @NotNull(message = "Assignee ID is required")
    public Long assigneeId;
}
