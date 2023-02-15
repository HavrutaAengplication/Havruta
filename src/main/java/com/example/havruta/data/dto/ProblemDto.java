package com.example.havruta.data.dto;

import lombok.*;

import java.util.List;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProblemDto {
    private List<CategoryIdDto> categoryIdList;
    private Boolean problemType;
    private String problemQuestion;
    private List<ItemDto> problemCandidate;
    private String problemAnswer;
    private List<ImageDto> problemImage;
}
