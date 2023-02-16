package com.example.havruta.data.dto;

import lombok.*;

import java.util.List;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SpecificGroupResponseDto {
    private String groupName;
    private List<CategoryDto> categoryList;
    private Boolean isAdmin;
    private Boolean isMember;
}
