package com.example.havruta.data.repository;

import com.example.havruta.data.entity.CategoryEntity;
import com.example.havruta.data.entity.CategoryProblemEntity;
import com.example.havruta.data.entity.serializable.CategoryProblemId;
import com.example.havruta.data.entity.ProblemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface CategoryProblemRepository extends JpaRepository<CategoryProblemEntity, CategoryProblemId> {

    public List<CategoryProblemEntity> findById_CategoryId(Integer categoryId);
    public List<CategoryProblemEntity> findAllById_ProblemId(Integer problemId);

    @Transactional
    void deleteAllById_CategoryId(Integer id_categoryId);
}
