package com.example.library.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.library.DTO.Member.Request.RequestDataMember;
import com.example.library.DTO.Member.Response.CoreResponse;
import com.example.library.Services.MemberService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/member")
@AllArgsConstructor

public class MemberController {
    private final MemberService memberService;

    @PostMapping("/add")
    public ResponseEntity<CoreResponse> addMember(@RequestBody @Valid RequestDataMember member) {
        log.info("Adding member: {}", member);

        // Call the service to add the member
        ResponseEntity<CoreResponse> response = memberService.addMember(member);

        return response;
    }

    @PutMapping("/edit/{idMember}")
    public ResponseEntity<CoreResponse> editMember(@PathVariable @Valid Integer idMember, @RequestBody RequestDataMember updateMember) {
        log.info("Editing member with ID : {}", idMember);

        // Call the service to edit the member
        ResponseEntity<CoreResponse> response = memberService.editMember(idMember, updateMember);

        return response;
    }

    @DeleteMapping("/delete/{idMember}")
    public ResponseEntity<CoreResponse> deleteMember(@PathVariable @Valid Integer idMember) {
        log.info("Deleting member with ID : {}", idMember);

        // Call the service to delete the member
        ResponseEntity<CoreResponse> response = memberService.deleteMember(idMember);

        return response;
    }

    @GetMapping("/get/{idMember}")
    public ResponseEntity<CoreResponse> getMember(@PathVariable @Valid Integer idMember) {
        log.info("Getting member with ID : {}", idMember);

        //Call the service to get the member
        ResponseEntity<CoreResponse> response = memberService.getMember(idMember);

        return response;
    }
}
