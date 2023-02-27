package com.example.havruta.data.dto;

import lombok.*;

import java.util.List;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryProblemDto{
    private Integer problemId;
    private String problemType;
    private String problemQuestion;
    private List<ItemDto> problemCandidate;
    private String problemAnswer;
    private List<ImageDto> problemImage;
}