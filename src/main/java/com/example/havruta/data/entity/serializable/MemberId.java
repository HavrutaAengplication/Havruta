package com.example.havruta.data.entity.serializable;

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
public class MemberId implements Serializable {
    @Column(name = "user_ID")
    private Integer userId;

    @Column(name = "group_ID")
    private Integer groupId;

    @Override
    public boolean equals(Object o){
        if(o == this){
            return true;
        }
        if(!(o instanceof MemberId)){
            return false;
        }
        final MemberId o2 = (MemberId) o;
        return o2.getUserId().equals(getUserId()) && o2.getGroupId().equals(getGroupId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, groupId);
    }
}