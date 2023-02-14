package com.example.havruta.data.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Table(name = "problems")
@Entity
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProblemEntity {
    @Id
    @Column(name = "problem_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int problemId;

    @Column(name = "user_ID")
    private Integer userId;

    @Column(name = "type")
    private String problemType;

    @Column(name = "question")
    private String problemQuestion;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "item")
    private List<ItemEntity> problemCandidate;

    @Column(name = "answer")
    private String problemAnswer;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "image")
    private List<ImageEntity> problemImage;

}
