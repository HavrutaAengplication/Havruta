package com.example.havruta.service;

import com.example.havruta.data.dto.*;

import java.util.List;

public interface HavrutaService {

    public ResponseDto signUp(SignInRequestDto reqbody);
    public GroupListResponseDto mainPage();
    public UserNameDto login(LoginRequestDto req);
    public ResponseDto newGroup(String token, NewGroupRequestDto reqbody);
    public MyPageResponseDto myPage(String token);

    public ResponseDto changeUserName(String token, String userName);
    public ResponseDto deleteUser(String token);
    public MyProblemListDto getMyProblemList(String token);
    public ResponseDto updateProblem(String token, Integer problemId, ProblemDto problemDto);

    public ResponseDto deleteProblem(String token, Integer problemId);
}
