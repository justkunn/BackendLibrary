package com.example.library.DTO.Member.Response;

import lombok.*;

@Data
public class ResponseDataMember {
    private Integer idMember;
    private String memberName;
    private Integer age;
    private Integer phoneNumber;
    private String email;
    private String level;
}