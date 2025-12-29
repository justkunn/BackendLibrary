package com.example.library.DTO.Employee.Response;

import lombok.Data;

@Data

public class MainResponse {
    private String Status;
    private String message;
    private ResponseDataEmployee data;
    private Integer ResponseCode;
    private String timestamp;
}