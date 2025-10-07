package com.dbelanger.spring.agileapi.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class SprintRequest {
    
    @NotBlank(message = "Sprint name is required")
    @Size(min = 3, max = 100, message = "Sprint name must be between 3 and 100 characters")
    public String name;
    
    @NotNull(message = "Start date is required")
    public LocalDate startDate;
    
    @NotNull(message = "End date is required")
    public LocalDate endDate;
    
    @AssertTrue(message = "End date must be after start date")
    private boolean isEndDateAfterStartDate() {
        if (startDate == null || endDate == null) {
            return true; // Let @NotNull handle null validation
        }
        return endDate.isAfter(startDate);
    }
}
