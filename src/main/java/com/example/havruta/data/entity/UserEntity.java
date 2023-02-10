package com.example.havruta.data.entity;

import lombok.*;

import javax.persistence.*;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_ID")
    private Integer userId;
    @Column(nullable = false, name = "user_name")
    private String userName;
    @Column(nullable = false, name = "user_email")
    private String userEmail;
}
