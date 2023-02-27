package com.example.havruta.data.entity.serializable;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

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
}
