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
public class MemberEntity {

    @EmbeddedId
    private MemberId id;

    @ManyToOne
    @MapsId("userId")
    private UserEntity userEntity;

    @ManyToOne
    @MapsId("groupId")
    private GroupEntity groupEntity;

    @Column(name = "is_admin", nullable = false)
    private Integer isAdmin;

    @Column(name = "is_member", nullable = false)
    private Integer isMember;
}
