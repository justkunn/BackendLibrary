package com.example.library.Services;

import java.text.SimpleDateFormat;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.library.DTO.Member.Request.RequestDataMember;
import com.example.library.DTO.Member.Response.CoreResponse;
import com.example.library.DTO.Member.Response.ResponseDataMember;
import com.example.library.Entity.MemberEntity;
import com.example.library.Repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor

public class MemberService {
    private final MemberRepository memberRepository;

    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    String formatDate = sdf.format(new java.util.Date());

    public ResponseEntity<CoreResponse> addMember(RequestDataMember memberRequest) {
        log.info("preparing to add member with data: {}", memberRequest);

        try {
            log.info("mapping RequestDataMember to MemberEntity");
            MemberEntity memberEntity = new MemberEntity();
            MemberEntity member = mapToEntity(memberEntity, memberRequest);

            // save to database
            log.info("saving member to database : {}", memberEntity);
            MemberEntity savedEntity = memberRepository.save(member);
            log.info("set the response : {}", savedEntity);
            CoreResponse responseDataMember = mapToResponse(savedEntity, HttpStatus.OK);

            return ResponseEntity.ok(responseDataMember);
        } catch (Exception e) {
            log.error("error occurred while adding member: {}", e.getMessage());

            // map error response
            CoreResponse errorResponse = mapToResponse(null, HttpStatus.INTERNAL_SERVER_ERROR);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    public ResponseEntity<CoreResponse> editMember(Integer idMember, RequestDataMember updateMember) {
        log.info("preparing to edit member with ID : {}", idMember);

        try {
            // find the member ID
            log.info("finding member with ID : {}", idMember);
            MemberEntity memberEntity = memberRepository.findByIdMember(idMember)
                    .orElseThrow(() -> new RuntimeException("Member not found with ID : {}" + idMember));
                    
            mapToEntity(memberEntity, updateMember);
            memberRepository.save(memberEntity);
            log.info("set the response : {}", updateMember);
            CoreResponse responseDataMember = mapToResponse(memberEntity, HttpStatus.OK);
            return ResponseEntity.ok(responseDataMember);
        } catch (Exception e) {
            log.error("error occurred while editing member: {}", e.getMessage());

            // map error response
            CoreResponse errorResponse = mapToResponse(null, HttpStatus.INTERNAL_SERVER_ERROR);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    public ResponseEntity<CoreResponse> deleteMember(Integer idMember) {
        log.info("preparing to delete member with ID : {}", idMember);

        try {
            log.info("finding member with ID : {}", idMember);
            MemberEntity memberEntity = memberRepository.findByIdMember(idMember)
                    .orElseThrow(() -> new RuntimeException("Member no found with ID : {}" + idMember));
            log.info("deleting member witg ID  : {}", memberEntity);

            CoreResponse coreResponse = mapToResponse(memberEntity, HttpStatus.OK);
            return ResponseEntity.ok(coreResponse);

        } catch (Exception e) {
            log.error("error occurred while deleting member : {}", e.getMessage());

            CoreResponse errorResponse = mapToResponse(null, HttpStatus.INTERNAL_SERVER_ERROR);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);

        }
    }

    public ResponseEntity<CoreResponse> getMember(Integer idMember) {
        log.info("getting data member with ID : {}", idMember);

        try {
            log.info("trying to find membe with ID : {}", idMember);
            MemberEntity memberEntity = memberRepository.findByIdMember(idMember)
            .orElseThrow(() -> new RuntimeException("Member with Id {} not found" + idMember));

            log.info("mappring member to response :");
            CoreResponse coreResponse = mapToResponse(memberEntity, HttpStatus.OK);
            return ResponseEntity.ok(coreResponse);
        } catch (Exception e) {
            log.error("error occurred while getting member : {}", e.getMessage());

            CoreResponse errResponse = mapToResponse(null, null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errResponse);
        }
    }

    public MemberEntity mapToEntity(MemberEntity memberEntity, RequestDataMember member) {
        log.info("preparing to map RequestDataMember from request : {}", memberEntity);
        memberEntity.setMemberName(member.getMemberName());
        memberEntity.setAge(member.getAge());
        memberEntity.setPhoneNumber(member.getPhoneNumber());
        memberEntity.setEmail(member.getEmail());
        memberEntity.setLevel(member.getLevel());
        log.info("mapping RequestDataMember to MemberEntity: {}", memberEntity);

        return memberEntity;
    }

    public CoreResponse mapToResponse(MemberEntity memberEntity, HttpStatus status) {
        CoreResponse coreResponse = new CoreResponse();

        if (memberEntity != null) {
            ResponseDataMember responseDataMember = new ResponseDataMember();
            responseDataMember.setIdMember(memberEntity.getIdMember());
            responseDataMember.setMemberName(memberEntity.getMemberName());
            responseDataMember.setAge(memberEntity.getAge());
            responseDataMember.setPhoneNumber(memberEntity.getPhoneNumber());
            responseDataMember.setEmail(memberEntity.getEmail());
            responseDataMember.setLevel(memberEntity.getLevel());
            coreResponse.setData(responseDataMember);
        } else {
            coreResponse.setData(null);
        }

        if (status == HttpStatus.OK) {
            coreResponse.setStatus("Success");
            coreResponse.setMessage("Success for adding member");
            coreResponse.setResponseCode("200");
        } else if (status == HttpStatus.INTERNAL_SERVER_ERROR) {
            coreResponse.setStatus("Failed");
            coreResponse.setMessage("Failed for adding member");
            coreResponse.setResponseCode("500");
        } else {
            coreResponse.setStatus("Unknown");
            coreResponse.setMessage("Unknown status");
            coreResponse.setResponseCode("400");
        }

        coreResponse.setTimestamp(formatDate);
        return coreResponse;
    }
}