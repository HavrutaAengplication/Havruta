package com.example.havruta.data.entity.serializable;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class CategoryProblemId implements Serializable {
    @Column(name = "category_ID")
    private Integer categoryId;

    @Column(name = "problem_ID")
    private Integer problemId;
}
