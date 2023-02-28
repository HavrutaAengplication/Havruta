package com.example.havruta.data.repository;

import com.example.havruta.data.entity.MemberEntity;
import com.example.havruta.data.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends JpaRepository<MemberEntity,Integer> {
    List<MemberEntity> findByUserEntity(UserEntity userEntity);
}
