package com.example.havruta.controller;

import com.example.havruta.data.dto.*;
import com.example.havruta.service.WonbinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/groups/{groupId}")
public class WonbinController {
    private final WonbinService wonbinService;

    @Autowired
    public WonbinController(WonbinService wonbinService) {this.wonbinService = wonbinService;}

    @GetMapping("")
    public ResponseEntity<SpecificGroupResponseDto> specificGroupController(
            @RequestHeader("Authorization") String token,
            @PathVariable("groupId") Integer groupId
    ){
        //넘겨줄 때 사용자 정보도 넘어가야 됨
        SpecificGroupResponseDto dto = wonbinService.specificGroupPage(groupId);
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                //.location(URI.create("/"))
                .body(dto);
    }

    @GetMapping("/admin")
    public ResponseEntity<AdminResponseDto> adminController(
            @RequestHeader("Authorization") String token,
            @PathVariable("groupId") Integer groupId
    ){
        //넘겨줄 때 사용자 정보도 넘어가야 됨
        AdminResponseDto dto = wonbinService.admin(groupId);
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                //.location(URI.create("/"))
                .body(dto);
    }

    @GetMapping("/members")
    public ResponseEntity<AdminMembersResponseDto> adminMembersController(
            @RequestHeader("Authorization") String token,
            @PathVariable("groupId") Integer groupId
    ){
        //넘겨줄 때 사용자 정보도 넘어가야 됨
        AdminMembersResponseDto dto = wonbinService.adminMembers(groupId);
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                //.location(URI.create("/"))
                .body(dto);
    }

    @PutMapping("/members")
    public ResponseEntity<ResponseDto> designateAdminController(
            @RequestHeader("Authorization") String token,
            @RequestBody designateAdminRequestDto reqBody,
            @PathVariable("groupId") Integer groupId
    ){
        //넘겨줄 때 사용자 정보도 넘어가야 됨
        ResponseDto dto = wonbinService.designateAdmin(reqBody.getNewAdminId(), groupId);
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                //.location(URI.create("/"))
                .body(dto);
    }

    @DeleteMapping("/members/{userId}")
    public ResponseEntity<ResponseDto> dropMemberController(
            @RequestHeader("Authorization") String token,
            @PathVariable("groupId") Integer groupId,
            @PathVariable("userId") Integer userId
    ){
        //넘겨줄 때 사용자 정보도 넘어가야 됨
        ResponseDto dto = wonbinService.dropMember(userId, groupId);
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                //.location(URI.create("/"))
                .body(dto);
    }

    @PutMapping("/members/{userId}")
    public ResponseEntity<ResponseDto> confirmController(
            @RequestHeader("Authorization") String token,
            @PathVariable("groupId") Integer groupId,
            @PathVariable("userId") Integer userId
    ){
        //넘겨줄 때 사용자 정보도 넘어가야 됨
        ResponseDto dto = wonbinService.confirm(userId, groupId);
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                //.location(URI.create("/"))
                .body(dto);
    }

    @DeleteMapping("")
    public ResponseEntity<ResponseDto> deleteGroupController(
            @RequestHeader("Authorization") String token,
            @PathVariable("groupId") Integer groupId
    ){
        //넘겨줄 때 사용자 정보도 넘어가야 됨
        ResponseDto dto = wonbinService.deleteGroup(groupId);
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                //.location(URI.create("/"))
                .body(dto);
    }

    @PutMapping("")
    public ResponseEntity<ResponseDto> updateGroupController(
            @RequestHeader("Authorization") String token,
            @RequestBody updateGroupRequestDto reqBody,
            @PathVariable("groupId") Integer groupId
    ){
        //넘겨줄 때 사용자 정보도 넘어가야 됨
        ResponseDto dto = wonbinService.updateGroup(reqBody.getNewGroupName(), groupId);
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                //.location(URI.create("/"))
                .body(dto);
    }
}
