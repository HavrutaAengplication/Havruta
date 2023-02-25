package com.example.havruta.controller;

import com.example.havruta.data.dto.LoginRequestDto;
import com.example.havruta.data.dto.ResponseDto;
import com.example.havruta.data.dto.SignInRequestDto;
import com.example.havruta.data.dto.UserNameDto;
import com.example.havruta.security.JwtUtil;
import com.example.havruta.service.HavrutaService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/users")
public class UsersController {
    private final HavrutaService havrutaService;
    private JwtUtil jwtUtil;

    public UsersController(HavrutaService havrutaService, JwtUtil jwtUtil) {
        this.havrutaService = havrutaService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/signup")
    public ResponseEntity<ResponseDto> signInController(
            @RequestBody SignInRequestDto reqbody) {
        ResponseDto dto = havrutaService.signIn(reqbody);

        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .location(URI.create("/targetpage"))
                .body(dto);
    }

    @PostMapping("/login")
    public ResponseEntity<UserNameDto> loginController(
            @RequestBody LoginRequestDto req){
        /*
            1. reqbody.googleToken validation
            2. email로 users 테이블에서 userId, userName 가져오기
            3. userId 로 JwtToken 생성
        */
        UserNameDto dto = havrutaService.login(req);
        final String jwt = jwtUtil.generateToken(123);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwt);

        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .headers(headers)
                .location(URI.create("/targetpage"))
                .body(dto);
    }

    @DeleteMapping("/logout")
    public ResponseEntity<ResponseDto> logoutController(
            @RequestHeader("Authorization") String token
    ){
        ResponseDto dto = new ResponseDto();
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .location(URI.create("/targetpage"))
                .body(dto);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<ResponseDto> createAuthenticationToken(){
        final String jwt = jwtUtil.generateToken(123);
        ResponseDto dto = new ResponseDto();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwt);
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .headers(headers)
                .location(URI.create("/targetpage"))
                .body(dto);
    }
    @GetMapping("/hello")
    public String hello(@RequestHeader("Authorization") String token) {
        Integer userId = jwtUtil.extractUserId(token);
        // Do something with the username
        return "Hello, " + userId + "!";
    }
}
