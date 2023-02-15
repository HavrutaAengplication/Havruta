package com.example.havruta.data.dto;

import java.util.List;

public class CategoryProblemDto{
    private String problemId;
    private String problemType;
    private String problemQuestion;
    private List<ItemDto> problemCandidate;
    private String problemAnswer;
    private List<ImageDto> problemImage;
}