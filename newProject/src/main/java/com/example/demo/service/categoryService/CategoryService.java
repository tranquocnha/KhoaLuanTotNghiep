package com.example.demo.service.categoryService;

import com.example.demo.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CategoryService {
    Page<Category> findAll(Pageable pageable);
    List<Category> findAll();
    void save(Category category);

    void delete(int idCategory);

    Category findById(int idCategory);

    List<Category> findByName(String nameCategory);
}
