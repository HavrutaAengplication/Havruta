package com.example.havruta.data.repository;

import com.example.havruta.data.entity.CategoryEntity;
import com.example.havruta.data.entity.CategoryProblemEntity;
import com.example.havruta.data.entity.serializable.CategoryProblemId;
import com.example.havruta.data.entity.ProblemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryProblemRepository extends JpaRepository<CategoryProblemEntity, CategoryProblemId> {
    public List<CategoryProblemEntity> findAllByCategoryEntity_CategoryId(Integer categoryId);

    public List<CategoryProblemEntity> findById_CategoryId(Integer categoryId);
    public List<CategoryProblemEntity> findAllByCategoryEntity_CategoryId(CategoryEntity categoryEntity);

    public void deleteAllById_CategoryId(Integer categroyId);

    public Optional<CategoryProblemEntity> findFirstByProblemEntity(ProblemEntity problemEntity);

    public List<CategoryProblemEntity> findAllByProblemEntity(ProblemEntity problemEntity);
}
