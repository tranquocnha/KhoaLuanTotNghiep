package com.example.demo.model.DTO;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Transient;

@Data
public class CategoryDTO {
    private String categoryName;

    @Transient
    private MultipartFile categoryImg;

    public CategoryDTO() {
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public MultipartFile getCategoryImg() {
        return categoryImg;
    }

    public void setCategoryImg(MultipartFile categoryImg) {
        this.categoryImg = categoryImg;
    }
}
