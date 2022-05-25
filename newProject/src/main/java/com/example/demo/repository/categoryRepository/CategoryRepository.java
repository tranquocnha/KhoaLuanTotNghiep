package com.example.demo.repository.categoryRepository;

import com.example.demo.model.Category;
import org.hibernate.dialect.pagination.TopLimitHandler;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    List<Category> findCategoryByCategoryName(String name);
    List<Category> findAll();

    @Query(value = "select DISTINCT c.* from category c inner join product p on p.id_category=c.id_category inner join `account` a on a.id_account=p.`account` inner join acc_user ac on ac.`account`=a.id_account where ac.id_user=:id", nativeQuery = true)
    List<Category> findCategoryByIdUsers(int id);


    @Query(value = "select * from category order by category_name DESC  LIMIT 5;",nativeQuery = true)
    List<Category> findTop();
}
