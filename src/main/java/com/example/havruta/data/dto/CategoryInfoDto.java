package com.example.havruta.data.dto;

import lombok.*;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryInfoDto {
    private Integer parentCategoryId;
    private String categoryName;
}
