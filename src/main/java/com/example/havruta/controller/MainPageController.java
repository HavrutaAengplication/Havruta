package com.example.havruta.controller;

import com.example.havruta.data.dto.GroupDto;
import com.example.havruta.data.dto.GroupListResponseDto;
import com.example.havruta.data.dto.NewGroupRequestDto;
import com.example.havruta.data.dto.ResponseDto;
import com.example.havruta.data.entity.GroupEntity;
import com.example.havruta.service.HavrutaService;
import com.example.havruta.service.HavrutaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/home")
public class MainPageController {
    private final HavrutaService havrutaService;
    @Autowired
    public MainPageController(HavrutaService havrutaService) {
        this.havrutaService = havrutaService;
    }

    @GetMapping("")
    public ResponseEntity<GroupListResponseDto> mainPageController(
            @RequestHeader("Authorization") String token
    ){
        GroupListResponseDto dto = havrutaService.mainPage();
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .location(URI.create("/mainpage"))
                .body(dto);
    }

    @PostMapping("new")
    public ResponseEntity<ResponseDto> newGroupController(
            @RequestHeader("Authorization") String token,
            @RequestBody NewGroupRequestDto reqbody
    ){
        ResponseDto dto = havrutaService.newGroup(token,reqbody);
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .location(URI.create("/mainpage"))
                .body(dto);
    }
}
