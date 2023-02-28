package com.example.havruta.data.entity;

import com.mysql.cj.xdevapi.JsonString;
import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.vladmihalcea.hibernate.type.json.JsonType;
import org.hibernate.annotations.TypeDefs;


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
    private Integer problemId;

    @ManyToOne(targetEntity = UserEntity.class)
    @JoinColumn(name = "user_ID", nullable = false)
    private UserEntity userId;

    @Column(name = "type")
    private Integer problemType;

    @Column(name = "question")
    private String problemQuestion;

    @Type(type = "json")
    @Column(name = "item", columnDefinition = "json")
    private Map<Integer, String> problemCandidate = new HashMap<>();
    @Column(name = "answer")
    private String problemAnswer;

    @Type(type = "json")
    @Column(name = "image", columnDefinition = "json")
    private Map<Integer, String> problemImage = new HashMap<>();
}
