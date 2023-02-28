package com.example.havruta.data.repository;

import com.example.havruta.data.entity.CategoryClosureEntity;
import com.example.havruta.data.entity.MemberEntity;
import com.example.havruta.data.entity.serializable.MemberId;
import com.example.havruta.data.entity.MemberEntity;
import com.example.havruta.data.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface MemberRepository extends JpaRepository<MemberEntity,MemberId> {
    List<MemberEntity> findByUserEntity(UserEntity userEntity);

    public List<MemberEntity> findById_GroupId(Integer groupId);
    @Transactional
    public void deleteById_groupId(Integer groupId);
}
