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
//@Data
@Embeddable
public class MemberId implements Serializable {

    //private static final long serialVersionUID = -2929789292155268166L;

    @Column(name = "user_ID")
    private Integer userId;

    @Column(name = "group_ID")
    private Integer groupId;
}