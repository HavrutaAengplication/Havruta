package com.example.havruta.data.dto;

import lombok.*;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {
    private Integer catergoryId;
    private String catergoryName;
    private Integer depth;
}
