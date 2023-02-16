package com.example.havruta.service;

import com.example.havruta.data.dto.AdminMembersResponseDto;
import com.example.havruta.data.dto.AdminResponseDto;
import com.example.havruta.data.dto.ResponseDto;
import com.example.havruta.data.dto.SpecificGroupResponseDto;

public interface WonbinService {
    public SpecificGroupResponseDto specificGroupPage(Integer groupId);
    public AdminResponseDto admin(Integer groupId);
    public AdminMembersResponseDto adminMembers(Integer groupId);
    public ResponseDto designateAdmin(Integer newAdminId, Integer groupId);
    public ResponseDto dropMember(Integer userId, Integer groupId);
    public ResponseDto confirm(Integer userId, Integer groupId);

    public ResponseDto deleteGroup(Integer groupId);

    public ResponseDto updateGroup(String newGroupName, Integer groupId);
}
