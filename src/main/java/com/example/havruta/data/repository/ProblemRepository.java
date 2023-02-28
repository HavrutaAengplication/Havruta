package com.example.havruta.data.repository;

import com.example.havruta.data.entity.ProblemEntity;
import com.example.havruta.data.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProblemRepository extends JpaRepository<ProblemEntity, Integer> {
    List<ProblemEntity> findAllByUserId (UserEntity userEntity);
}
