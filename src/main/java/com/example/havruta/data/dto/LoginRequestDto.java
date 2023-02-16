package com.example.havruta.data.dto;

import lombok.*;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDto {
    private String googleToken;
}
