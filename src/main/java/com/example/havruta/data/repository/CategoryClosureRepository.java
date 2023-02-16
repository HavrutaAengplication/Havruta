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
    @Query(value = "INSERT INTO category_closure (parent_ID, child_ID, depth) " +
            "select c.parent_ID, :newId, c.depth + 1 from category_closure as c " +
            "where c.child_ID = :parentId union all select :newId, :newId, 0",
            nativeQuery = true)
    void createCategory(@Param("newId") Integer newId, @Param("parentId") Integer parentId);

    @Query(value =
            "DROP TABLE IF EXISTS temp_closure_table;" +
            "CREATE TABLE temp_closure_table " +
            "SELECT child_ID " +
            "FROM category_closure " +
            "WHERE parent_ID = 3;" +
            "DELETE FROM category_closure " +
            "WHERE parent_ID IN (" +
            "  SELECT child_ID " +
            "  FROM temp_closure_table " +
            ")" +
            "OR child_ID IN (" +
            "  SELECT child_ID" +
            "  FROM temp_closure_table" +
            ");" +
            "DELETE FROM categories " +
            "WHERE category_ID IN (" +
            "    SELECT child_ID" +
            "    FROM temp_closure_table" +
            ");" +
            "DELETE FROM category_problems " +
            "WHERE category_ID IN (" +
            "    SELECT child_ID" +
            "    FROM temp_closure_table" +
            ");" +
            "DROP TABLE IF EXISTS temp_closure_table;",
            nativeQuery = true)
    void removeCategory(@Param("curId") Integer curId);

    @Query(value =
            "DROP TABLE IF EXISTS temp_from_table;" +
            "DROP TABLE IF EXISTS temp_to_table;" +
            "CREATE TABLE temp_from_table " +
            "SELECT child_ID, depth as from_depth " +
            "FROM category_closure "+
            "WHERE parent_ID = 2;" +
            "SET @temp_depth = 1;" +
            "SELECT @temp_depth = depth " +
            "FROM category_closure " +
            "WHERE parent_ID = 1 AND child_ID = 3; " +
            "CREATE TABLE temp_to_table " +
            "SELECT parent_ID, depth as to_depth " +
            "FROM category_closure " +
            "WHERE child_ID = 3; " +
            "DELETE FROM category_closure " +
            "WHERE child_ID IN ( " +
            "  SELECT child_ID " +
            "  FROM temp_from_table " +
            ")" +
            "AND parent_ID NOT IN (" +
            "  SELECT child_ID" +
            "  FROM temp_from_table" +
            ");" +
            "INSERT INTO category_closure " +
            "(SELECT parent_ID, child_ID, from_depth + to_depth + 1 FROM ( " +
            "SELECT * FROM temp_to_table " +
            "INNER JOIN temp_from_table " +
            ") as a); " +
            "DROP TABLE IF EXISTS temp_from_table; " +
            "DROP TABLE IF EXISTS temp_to_table;", nativeQuery = true)
    void updateCategory(@Param("curId") Integer curId, @Param("parentId") Integer parentId, @Param("rootId") Integer rootId);
}
