package com.example.havruta.data.entity.serializable;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import java.io.Serializable;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class ClosureId implements Serializable {

    @Column(name = "parent_ID")
    private Integer parentId;

    @Column(name = "child_ID")
    private Integer childId;
}
