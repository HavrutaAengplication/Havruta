package com.example.havruta.data.dto;

import lombok.*;

import java.util.List;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MyProblemListDto {
    List<MyProblemDto> myProblemDtoList;
}
