package com.example.havruta.data.entity;

import lombok.*;

import javax.persistence.*;
@Table(name = "group_table")
@Entity
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GroupEntity{
    @Id
    @Column(name = "group_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer groupId;

    @Column(name = "group_name", length = 30, nullable = false)
    private String groupName;

    @OneToOne
    @JoinColumn(name = "root_category_ID", referencedColumnName = "category_ID")
    private CategoryEntity rootCategoryId;
}