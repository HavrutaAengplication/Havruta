package com.example.havruta.data.dto;

import lombok.*;

import java.util.List;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MyProblemDto {
    private Integer groupId;
    private String groupName;
    private List<PathDto> categoryIdList;
    private Integer problemType;
    private String problemQuestion;
    private List<ItemDto> problemCandidate;
    private String problemAnswer;
    private List<ImageDto> problemImage;
}
