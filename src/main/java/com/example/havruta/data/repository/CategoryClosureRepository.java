package com.example.havruta.data.repository;

import com.example.havruta.data.entity.CategoryClosureEntity;
import com.example.havruta.data.entity.serializable.ClosureId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface CategoryClosureRepository extends JpaRepository<CategoryClosureEntity, ClosureId> {

    public List<CategoryClosureEntity> findById_ParentId(Integer parentId);

    public List<Integer> findId_ChildIdById_ParentId(Integer parentId);

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

    /* for removing a category */
    @Modifying
    @Transactional
    @Query(value = "DROP TABLE IF EXISTS temp_closure_table", nativeQuery = true)
    void removeCategory_dropTempClosureTable();

    @Modifying
    @Transactional
    @Query(value = "CREATE TABLE temp_closure_table AS SELECT child_category_id FROM category_closure WHERE parent_category_id = :curId", nativeQuery = true)
    void removeCategory_createTempClosureTable(@Param("curId") Integer curId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM category_closure WHERE parent_category_id IN (SELECT child_category_id FROM temp_closure_table) OR child_category_id IN (SELECT child_category_id FROM temp_closure_table)", nativeQuery = true)
    void removeCategory_deleteFromCategoryClosure();

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM categories WHERE category_id IN (SELECT child_category_id FROM temp_closure_table)", nativeQuery = true)
    void removeCategory_deleteFromCategories();

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM category_problems WHERE category_entity_category_id IN (SELECT child_category_id FROM temp_closure_table)", nativeQuery = true)
    void removeCategory_deleteFromCategoryProblems();

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
