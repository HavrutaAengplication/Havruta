package com.example.havruta.data.repository;

import com.example.havruta.data.entity.CategoryEntity;
import com.example.havruta.data.entity.GroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GroupRepository extends JpaRepository<GroupEntity,Integer> {
    Optional<GroupEntity> findByRootCategoryId(CategoryEntity categoryEntity);
}
