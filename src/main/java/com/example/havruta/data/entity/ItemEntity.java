package com.example.havruta.data.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ItemEntity {
    @Id
    private String item;
}
