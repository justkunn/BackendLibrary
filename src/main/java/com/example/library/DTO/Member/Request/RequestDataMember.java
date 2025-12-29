package com.example.library.DTO.Member.Request;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
public class RequestDataMember {
    
    @NotBlank(message = "Member name is required")
    private String memberName;

    @Min(value = 1, message = "Age must be greater than 0")
    private Integer age;

    @NotNull(message = "Phone number is required")
    private Integer phoneNumber;

    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Level is required")
    private String level;
}