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
    @JoinColumn(name = "user_ID", nullable = true)
    private UserEntity userId;

    @Column(name = "type")
    private Integer problemType;

    @Column(name = "question")
    private String problemQuestion;

    @Column(name = "item", columnDefinition = "json")
    @Convert(converter = JsonConverter.class)
    private Map<Integer, String> problemCandidate = new HashMap<>();
    @Column(name = "answer")
    private String problemAnswer;

    @Column(name = "image", columnDefinition = "json")
    @Convert(converter = JsonConverter.class)
    private Map<Integer, String> problemImage = new HashMap<>();

    @PostLoad
    private void initializeJsonFields() {
        if (problemCandidate == null) {
            problemCandidate = new HashMap<>();
        }
        if (problemImage == null) {
            problemImage = new HashMap<>();
        }
    }
}
