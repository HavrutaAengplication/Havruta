package com.example.havruta.data.entity.serializable;

import com.example.havruta.data.entity.CategoryProblemEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class CategoryProblemId implements Serializable {
    @Column(name = "category_ID")
    private Integer categoryId;

    @Column(name = "problem_ID")
    private Integer problemId;

    @Override
    public boolean equals(Object o){
        if(o == this){
            return true;
        }
        if(!(o instanceof CategoryProblemId)){
            return false;
        }
        final CategoryProblemId o2 = (CategoryProblemId) o;
        return o2.getCategoryId().equals(getCategoryId()) && o2.getProblemId().equals(getProblemId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(categoryId, problemId);
    }
}