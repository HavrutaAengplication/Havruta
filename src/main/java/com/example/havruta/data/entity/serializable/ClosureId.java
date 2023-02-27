package com.example.havruta.data.entity.serializable;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import java.io.Serializable;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o){
        if(o == this){
            return true;
        }
        if(!(o instanceof ClosureId)){
            return false;
        }
        final ClosureId o2 = (ClosureId) o;
        return o2.getChildId().equals(getChildId()) && o2.getParentId().equals(getParentId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(parentId, childId);
    }
}
