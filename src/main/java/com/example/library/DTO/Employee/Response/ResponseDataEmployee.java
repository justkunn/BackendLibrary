package com.example.library.DTO.Employee.Response;

import lombok.*;

@Data
public class ResponseDataEmployee {
    private Integer idEmployee;
    private String employeeName;
    private Integer age;
    private String email;
    private Integer phoneNumber;
    private String position;
}