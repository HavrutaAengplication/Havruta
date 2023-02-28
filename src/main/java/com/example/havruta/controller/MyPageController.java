package com.example.havruta.controller;

import com.example.havruta.data.dto.*;
import com.example.havruta.service.HavrutaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/mypage")
public class MyPageController {
    private final HavrutaService havrutaService;

    public MyPageController(HavrutaService havrutaService) {
        this.havrutaService = havrutaService;
    }

    @GetMapping("")
    public ResponseEntity<MyPageResponseDto> myPageController(
            @RequestHeader("Authorization") String token
    ){
        MyPageResponseDto dto = havrutaService.myPage(token);
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
        ResponseDto dto = havrutaService.changeUserName(token, reqbody.getUserName());
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .location(URI.create("/targetPageURI"))
                .body(dto);
    }

    @DeleteMapping("")
    public ResponseEntity<ResponseDto> deleteUserController(
            @RequestHeader("Authorization") String token
    ){
        ResponseDto dto = havrutaService.deleteUser(token);
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .location(URI.create("/targetPageURI"))
                .body(dto);
    }

    @GetMapping("/problems")
    public ResponseEntity<MyProblemListDto> myProblemController(
            @RequestHeader("Authorization") String token
    ){
        MyProblemListDto dto = havrutaService.getMyProblemList(token);
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
        ResponseDto dto = havrutaService.updateProblem(token,problemId,problemDto);
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
        ResponseDto dto = havrutaService.deleteProblem(token,problemId);
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .location(URI.create("/targetPageURI"))
                .body(dto);
    }

}
