package com.example.demo.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.util.Set;

@Entity
public class TempProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idProduct")
    private int idProduct;
    private String idAccount;

    private String  categoryName;
    private String color;
    @Min(value = 0)
    private int quantity;
    @Min(value = 0)
    private double price;

    @OneToMany(mappedBy = "tempProduct")
    private Set<TempBillProduct> tempBillProducts;

    private String productName;
    private String image1;
    private String image2;
    private String image3;
    @Column(length = 2500)
    private String description;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private String datePost;

    public TempProduct() {
    }

    public TempProduct(String idAccount, String categoryName, String color, int quantity, double price, String productName, String image1, String image2, String image3, String description, String datePost) {
        this.idAccount = idAccount;
        this.categoryName = categoryName;
        this.color = color;
        this.quantity = quantity;
        this.price = price;
        this.productName = productName;
        this.image1 = image1;
        this.image2 = image2;
        this.image3 = image3;
        this.description = description;
        this.datePost = datePost;
    }

    public int getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }

    public String getIdAccount() {
        return idAccount;
    }

    public void setIdAccount(String idAccount) {
        this.idAccount = idAccount;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Set<TempBillProduct> getTempBillProducts() {
        return tempBillProducts;
    }

    public void setTempBillProducts(Set<TempBillProduct> tempBillProducts) {
        this.tempBillProducts = tempBillProducts;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getImage3() {
        return image3;
    }

    public void setImage3(String image3) {
        this.image3 = image3;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDatePost() {
        return datePost;
    }

    public void setDatePost(String datePost) {
        this.datePost = datePost;
    }
}
