package com.example.havruta.data.entity;

import lombok.*;

import javax.persistence.*;

@Table(name = "users")
@Entity
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity{
    @Id
    @Column(name = "user_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @Column(name = "user_name", length = 20, nullable = false)
    private String userName;

    @Column(name = "user_email", length = 50, nullable = false)
    private String userEmail;
}