package com.example.demo.repository.categoryRepository;

import com.example.demo.model.Category;
import org.hibernate.dialect.pagination.TopLimitHandler;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    List<Category> findCategoryByCategoryName(String name);

    @Query(value = "select * from category order by category_name DESC  LIMIT 5;",nativeQuery = true)
    List<Category> findTop();
}
