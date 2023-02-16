package com.example.havruta.data.dto;

import lombok.*;

import java.util.List;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminMembersResponseDto {
    private List<MemberDto> memberList;
    private List<UserDto> joinList;
}
