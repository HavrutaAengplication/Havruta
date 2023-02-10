package com.example.havruta.controller;

import com.example.havruta.data.dto.GroupDto;
import com.example.havruta.data.dto.GroupListResponseDto;
import com.example.havruta.service.HavrutaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/home")
public class MainPageController {
    private final HavrutaServiceImpl havrutaService;

    @Autowired
    public MainPageController(HavrutaServiceImpl havrutaService) {
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
}
