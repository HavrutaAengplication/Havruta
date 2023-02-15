package com.example.havruta.data.dto;

import java.util.List;

public class ProblemDto{
    private List<CategoryIdDto> categoryIdList;
    private Boolean problemType;
    private String problemQuestion;
    private List<ItemDto> problemCandidate;
    private String problemAnswer;
    private List<ImageDto> problemImage;
}
