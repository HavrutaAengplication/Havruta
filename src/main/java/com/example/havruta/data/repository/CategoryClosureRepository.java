package com.example.havruta.data.repository;

import com.example.havruta.data.entity.CategoryClosureEntity;
import com.example.havruta.data.entity.CategoryEntity;
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
    public Optional<CategoryClosureEntity> findById_ChildId(Integer childId);

    /*
        Insert rows for a new category
        ex. new category id : 8
        (8, 8, 0) & (ancestors, 8, depth)
         */
    @Query(value = "INSERT INTO category_closure (parent_category_id, child_category_id, depth) " +
            "select c.parent_category_id, :newId, c.depth + 1 from category_closure as c " +
            "where c.child_category_id = :parentId union all select :newId, :newId, 0",
            nativeQuery = true)
    @Modifying
    @Transactional
    void createCategory(@Param("newId") Integer newId, @Param("parentId") Integer parentId);

    @Query(value =
            "DROP TABLE IF EXISTS temp_closure_table;" +
                    "CREATE TABLE temp_closure_table " +
                    "SELECT child_category_id " +
                    "FROM category_closure " +
                    "WHERE parent_category_id = 3;" +
                    "DELETE FROM category_closure " +
                    "WHERE parent_category_id IN (" +
                    "  SELECT child_category_id " +
                    "  FROM temp_closure_table " +
                    ")" +
                    "OR child_category_id IN (" +
                    "  SELECT child_category_id" +
                    "  FROM temp_closure_table" +
                    ");" +
                    "DELETE FROM categories " +
                    "WHERE category_ID IN (" +
                    "    SELECT child_category_id" +
                    "    FROM temp_closure_table" +
                    ");" +
                    "DELETE FROM category_problems " +
                    "WHERE category_ID IN (" +
                    "    SELECT child_category_id" +
                    "    FROM temp_closure_table" +
                    ");" +
                    "DROP TABLE IF EXISTS temp_closure_table;",
            nativeQuery = true)
    void removeCategory(@Param("curId") Integer curId);

    @Query(value =
            "DROP TABLE IF EXISTS temp_from_table;" +
                    "DROP TABLE IF EXISTS temp_to_table;" +
                    "CREATE TABLE temp_from_table " +
                    "SELECT child_category_id, depth as from_depth " +
                    "FROM category_closure "+
                    "WHERE parent_category_id = 2;" +
                    "SET @temp_depth = 1;" +
                    "SELECT @temp_depth = depth " +
                    "FROM category_closure " +
                    "WHERE parent_category_id = 1 AND child_category_id = 3; " +
                    "CREATE TABLE temp_to_table " +
                    "SELECT parent_category_id, depth as to_depth " +
                    "FROM category_closure " +
                    "WHERE child_category_id = 3; " +
                    "DELETE FROM category_closure " +
                    "WHERE child_category_id IN ( " +
                    "  SELECT child_category_id " +
                    "  FROM temp_from_table " +
                    ")" +
                    "AND parent_category_id NOT IN (" +
                    "  SELECT child_category_id" +
                    "  FROM temp_from_table" +
                    ");" +
                    "INSERT INTO category_closure " +
                    "(SELECT parent_category_id, child_category_id, from_depth + to_depth + 1 FROM ( " +
                    "SELECT * FROM temp_to_table " +
                    "INNER JOIN temp_from_table " +
                    ") as a); " +
                    "DROP TABLE IF EXISTS temp_from_table; " +
                    "DROP TABLE IF EXISTS temp_to_table;", nativeQuery = true)
    void updateCategory(@Param("curId") Integer curId, @Param("parentId") Integer parentId, @Param("rootId") Integer rootId);
}