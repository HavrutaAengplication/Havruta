package com.example.havruta.data.entity;

import com.example.havruta.data.entity.serializable.ClosureId;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "category_closure")
@Entity
public class CategoryClosureEntity {
    @EmbeddedId
    private ClosureId closureId;

    @Column(name = "depth")
    private int depth;
}


