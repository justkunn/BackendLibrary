package com.example.library.DTO.Employee.Request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.*;

@Data
public class RequestDataEmployee {

    @JsonProperty("employeeName")
    private String employeeName;

    @JsonProperty("age")
    private Integer age;

    @JsonProperty("email")
    private String email;

    @JsonProperty("phoneNumber")
    private Integer phoneNumber;

    @JsonProperty("position")
    private String position;
}