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

    public SpecificGroupResponseDto specificGroupPage(String token, Integer groupId);
    public AdminResponseDto admin(String token, Integer groupId);
    public AdminMembersResponseDto adminMembers(String token, Integer groupId);
    public ResponseDto designateAdmin(String token, Integer newAdminId, Integer groupId);
    public ResponseDto dropMember(String token, Integer userId, Integer groupId);
    public ResponseDto confirm(String token, Integer userId, Integer groupId);

    public ResponseDto deleteGroup(String token, Integer groupId);

    public ResponseDto updateGroup(String token, String newGroupName, Integer groupId);
}
