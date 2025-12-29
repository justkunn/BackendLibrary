package com.example.library.Entity; 
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "member")
@Data

public class MemberEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_member")
    private Integer idMember;

    @Column(name = "name")
    private String memberName;

    @Column(name = "age")
    private Integer age;

    @Column(name = "phone_number")
    private Integer phoneNumber;
    
    @Column(name = "email")
    private String email;

    @Column(name = "level")
    private String level;
}