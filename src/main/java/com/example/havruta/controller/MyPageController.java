package com.example.havruta.controller;

import com.example.havruta.data.dto.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/mypage")
public class MyPageController {
    @GetMapping("")
    public ResponseEntity<MyPageResponseDto> myPageController(
            @RequestHeader("Authorization") String token
    ){
        MyPageResponseDto dto = new MyPageResponseDto();
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .location(URI.create("/targetPageURI"))
                .body(dto);
    }

    @PutMapping("")
    public ResponseEntity<ResponseDto> changeUserNameController(
            @RequestHeader("Authorization") String token,
            @RequestBody UserNameDto reqbody
    ){
        ResponseDto dto =new ResponseDto();
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .location(URI.create("/targetPageURI"))
                .body(dto);
    }

    @DeleteMapping("")
    public ResponseEntity<ResponseDto> deleteUserController(
            @RequestHeader("Authorization") String token
    ){
        ResponseDto dto =new ResponseDto();
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .location(URI.create("/targetPageURI"))
                .body(dto);
    }

    @GetMapping("/problems")
    public ResponseEntity<ProblemListDto> myProblemController(
            @RequestHeader("Authorization") String token
    ){
        ProblemListDto dto = new ProblemListDto();
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .location(URI.create("/targetPageURI"))
                .body(dto);
    }

    @PutMapping("problems/{problemId}")
    public ResponseEntity<ResponseDto> updateProblemController(
            @RequestHeader("Authorization") String token,
            @RequestBody ProblemDto problemDto,
            @PathVariable Integer problemId
    ){
        ResponseDto dto = new ResponseDto();
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .location(URI.create("/targetPageURI"))
                .body(dto);
    }

    @DeleteMapping("problems/{problemId}")
    public ResponseEntity<ResponseDto> deleteProblemController(
            @RequestHeader("Authorization") String token,
            @PathVariable Integer problemId
    ){
        ResponseDto dto = new ResponseDto();
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .location(URI.create("/targetPageURI"))
                .body(dto);
    }

}
