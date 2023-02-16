package com.example.havruta.data.dto;

import lombok.*;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {
    private Integer userId;
    private String userName;
    private boolean isAdmin;
}
