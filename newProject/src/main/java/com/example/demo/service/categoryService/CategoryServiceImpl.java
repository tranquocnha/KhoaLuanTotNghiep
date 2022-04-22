package com.example.demo.service.categoryService;

import com.example.demo.model.Category;
import com.example.demo.repository.categoryRepository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public Page<Category> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable);
    }

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public void save(Category category) {
        categoryRepository.save(category);
    }

    @Override
    public void delete(int idCategory) {
        categoryRepository.deleteById(idCategory);
    }

    @Override
    public Category findById(int idCategory) {
        return categoryRepository.findById(idCategory).orElse(null);
    }

    @Override
    public List<Category> findByName(String nameCategory) {
        return categoryRepository.findCategoryByCategoryName(nameCategory);
    }
}
