package com.example.havruta.data.entity;

import com.example.havruta.data.entity.serializable.MemberId;
import lombok.*;

import javax.persistence.*;

@Table(name = "members")
@Entity
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//@IdClass(MemberId.class)
//@GeneratedValue for auto increment
public class MemberEntity {

    @EmbeddedId
    private MemberId id;
    //@Id
    @ManyToOne//(targetEntity = UserEntity.class)
    //@JoinColumn(name = "user_ID")//, nullable = false)
    @MapsId("userId")
    private UserEntity userEntity;

    //@Id
    @ManyToOne//(targetEntity = GroupEntity.class)
    //@JoinColumn(name = "group_ID")//, nullable = false)
    @MapsId("groupId")
    private GroupEntity groupEntity;

    @Column(name = "is_admin", nullable = false)
    private Integer isAdmin;

    @Column(name = "is_member", nullable = false)
    private Integer isMember;
}