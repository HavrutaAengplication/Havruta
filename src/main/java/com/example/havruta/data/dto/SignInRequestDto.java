package com.example.havruta.data.dto;

import lombok.*;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignInRequestDto {
    public String email;
    public String userName;
}
