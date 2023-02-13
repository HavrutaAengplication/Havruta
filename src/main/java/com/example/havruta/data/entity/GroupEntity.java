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
    private int groupId;

    @Column(name = "group_name", length = 30, nullable = false)
    private String groupName;

    //@OneToOne
    //@JoinColumn(name = "root_category_ID", referencedColumnName = "group_ID")
    @Column(name = "root_category_ID")
    private Integer rootCategoryId;
    //DB와의 통일성 위해 추가
    //관련된 API 있다면 수정해야 할 듯
    //예를 들어 newGroup? 찾아봤는데 service에 없길래 수정 못 함
}