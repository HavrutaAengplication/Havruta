package com.example.havruta.data.repository;

import com.example.havruta.data.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Integer> {
    @Override
    Optional<CategoryEntity> findById(Integer integer);

    @Transactional
    void deleteAllByCategoryId(Integer categoryId);
}
