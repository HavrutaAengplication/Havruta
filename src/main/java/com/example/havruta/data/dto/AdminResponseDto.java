package com.example.havruta.data.dto;

import lombok.*;

import java.util.List;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminResponseDto {
    private String groupName;
    private List<CategoryIdDto> categoryIdList;
}
