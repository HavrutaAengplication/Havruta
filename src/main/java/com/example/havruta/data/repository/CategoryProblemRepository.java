package com.example.havruta.data.repository;

import com.example.havruta.data.entity.CategoryProblemEntity;
import com.example.havruta.data.entity.serializable.CategoryProblemId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryProblemRepository extends JpaRepository<CategoryProblemEntity, CategoryProblemId> {
}
