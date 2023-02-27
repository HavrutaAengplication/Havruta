package com.example.havruta.data.repository;

import com.example.havruta.data.entity.MemberEntity;
import com.example.havruta.data.entity.serializable.MemberId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity, MemberId> {
    @Override
    Optional<MemberEntity> findById(MemberId memberId);
}
