package com.example.havruta.data.repository;

import com.example.havruta.data.entity.ProblemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProblemRepository extends JpaRepository<ProblemEntity, Integer> {
}
