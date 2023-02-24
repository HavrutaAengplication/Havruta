package com.example.havruta.data.entity;

import com.example.havruta.data.entity.serializable.ClosureId;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "category_closure")
@Entity
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryClosureEntity {
    @EmbeddedId
    private ClosureId id;

    @ManyToOne(targetEntity = CategoryEntity.class)
    @MapsId("parentId")
    private CategoryEntity parent;

    @ManyToOne(targetEntity = CategoryEntity.class)
    @MapsId("childId")
    private CategoryEntity child;

    @Column
    private Integer depth;
}