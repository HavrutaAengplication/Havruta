package com.example.havruta.data.entity;

import com.example.havruta.data.entity.serializable.ClosureId;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "category_closure")
@Entity
@IdClass(ClosureId.class)
public class CategoryClosureEntity {
    @Id
    @ManyToOne(targetEntity = CategoryEntity.class)
    @JoinColumn(name = "parent_ID", nullable = false)
    private CategoryEntity parentId;

    @Id
    @ManyToOne(targetEntity = CategoryEntity.class)
    @JoinColumn(name = "child_ID", nullable = false)
    private CategoryEntity childId;

    @Column(name = "depth", nullable = false)
    private int depth;
}

