package com.example.havruta.controller;

import com.example.havruta.data.dto.GroupDto;
import com.example.havruta.data.dto.GroupListResponseDto;
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
    @GetMapping("/hello")
    public ResponseEntity<GroupListResponseDto> mainPageController(
            @RequestHeader("Authorization") String token
    ){
        GroupListResponseDto dto = new GroupListResponseDto();
        GroupDto gd1 = new GroupDto(1,"First");
        GroupDto gd2 = new GroupDto(2,"Second");
        List<GroupDto> groupDto = new ArrayList<>(Arrays.asList(gd1, gd2));;

        dto.setGroupList(groupDto);
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .location(URI.create("/mainpage"))
                .body(dto);
    }
}
