package com.example.havruta.data.entity;

import com.example.havruta.data.entity.serializable.CategoryProblemId;
import lombok.*;

import javax.persistence.*;

@Table(name = "category_problems")
@Entity
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryProblemEntity {
    @EmbeddedId
    private CategoryProblemId id;

    @ManyToOne
    @MapsId("categoryId")
    private CategoryEntity categoryEntity;

    @ManyToOne
    @MapsId("problemId")
    private ProblemEntity problemEntity;
}