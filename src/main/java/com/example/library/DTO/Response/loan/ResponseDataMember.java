package com.example.library.DTO.Response.loan;

import lombok.Data;

@Data
public class ResponseDataMember {
    private Integer idMember;
    private String memberName;
    private Integer age;
    private Integer phoneNumber;
    private String email;
    private String level;
}
