package com.example.library.DTO.Member.Response;

import lombok.Data;

@Data
public class CoreResponse {
    private String status;
    private String message;
    private ResponseDataMember data;
    private String responseCode;
    private String timestamp;
}
