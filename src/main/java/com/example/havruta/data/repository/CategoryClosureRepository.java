package com.example.havruta.data.repository;

import com.example.havruta.data.entity.CategoryClosureEntity;
import com.example.havruta.data.entity.serializable.ClosureId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface CategoryClosureRepository extends JpaRepository<CategoryClosureEntity, ClosureId> {

    public List<CategoryClosureEntity> findById_ParentId(Integer parentId);
    public List<CategoryClosureEntity> findById_ChildId(Integer childId);
    public List<CategoryClosureEntity> findById_ParentIdAndDepth(Integer parentId, Integer depth);
    public Optional<CategoryClosureEntity> findById_ChildIdAndDepth(Integer childId, Integer depth);
    public List<CategoryClosureEntity> findAllById_ChildIdOrderByDepthDesc(Integer childId);
    @Transactional
    public void deleteAllById_ChildId(Integer childId);
    /*
        Insert rows for a new category
        ex. new category id : 8
        (8, 8, 0) & (ancestors, 8, depth)
         */
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO category_closure (parent_category_id, child_category_id, depth) " +
            "select c.parent_category_id, :newId, c.depth + 1 from category_closure as c " +
            "where c.child_category_id = :parentId union all select :newId, :newId, 0",
            nativeQuery = true)
    void createCategory(@Param("newId") Integer newId, @Param("parentId") Integer parentId);

    @Transactional
    List<CategoryClosureEntity> findAllById_ParentId(Integer childId);

    @Transactional
    void deleteAllByChild_CategoryId_OrParent_CategoryId(Integer child_categoryId, Integer parent_categoryId);

    @Modifying
    @Transactional
    @Query(value = "DROP TABLE IF EXISTS temp_from_table;", nativeQuery = true)
    void updateCategory_dropTempFromTable();

    @Modifying
    @Transactional
    @Query(value = "DROP TABLE IF EXISTS temp_to_table;", nativeQuery = true)
    void updateCategory_dropTempToTable();

    @Modifying
    @Transactional
    @Query(value = "CREATE TABLE temp_from_table " +
            "SELECT child_category_id, depth as from_depth " +
            "FROM category_closure "+
            "WHERE parent_category_id = :curId ;", nativeQuery = true)
    void updateCategory_createTempFromTable(@Param("curId") Integer curId);

    @Modifying
    @Transactional
    @Query(value = "CREATE TABLE temp_to_table " +
            "SELECT parent_category_id, depth as to_depth " +
            "FROM category_closure " +
            "WHERE child_category_id = :parentId ;", nativeQuery = true)
    void updateCategory_createTempToTable(@Param("parentId") Integer parentId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM category_closure " +
            "WHERE child_category_id IN ( " +
            "  SELECT child_category_id " +
            "  FROM temp_from_table " +
            ")" +
            "AND parent_category_id NOT IN (" +
            "  SELECT child_category_id" +
            "  FROM temp_from_table" +
            ");", nativeQuery = true)
    void updateCategory_deleteFromCategoryClosure();

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO category_closure " +
            "(SELECT from_depth + to_depth + 1, parent_category_id, child_category_id FROM ( " +
            "SELECT * FROM temp_to_table " +
            "INNER JOIN temp_from_table " +
            ") as a);", nativeQuery = true)
    void updateCategory_insertIntoCategoryClosure();
}
