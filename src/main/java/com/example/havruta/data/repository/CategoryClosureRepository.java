package com.example.havruta.data.repository;

import com.example.havruta.data.entity.CategoryClosureEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CategoryClosureRepository extends JpaRepository<CategoryClosureEntity, Integer> {
    /*
    Insert rows for a new category
    ex) new category id : 8
    (8, 8, 0) & (ancestors, 8, depth)
     */
    @Query(value = "INSERT INTO category_closure (parent_ID, child_ID, depth) select c.parent_ID, :newId, c.depth + 1 from category_closure as c where c.child_ID = :parentId union all select :newId, :newId, 0", nativeQuery = true)
    void saveNewCategory(@Param("newId") Integer newId, @Param("parentId") Integer parentId);

    @Query(value = "delete from category_closure as c where c.parent_ID = :curId or c.child_ID = :curId", nativeQuery = true)
    void removeCategory(@Param("curId") Integer curId);

    @Query(value = "", nativeQuery = true)
    void updateCategory(@Param("curId") Integer curId, @Param("parentId") Integer parentId);
}
