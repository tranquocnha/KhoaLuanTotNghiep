package com.example.demo.service.product;

import com.example.demo.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    Page<Product> findProductAndComment(Pageable pageable);

    List<Product> findAll();

    String findIdAccountByProduct(int id);
    Product findProductByIdProduct(int id);
    void save(Product product);

    Product findById(int id);

    Product findByIdStatus(String status, int idProduct);

    void delete(int id);

    int findByQuantity(int id);

    List<Product> findProduct(String idAccount);

    List<Product> findByNameApproved(String status, String nameProduct);

    List<Product> findByCategory(String status, int idCategory);

    List<Product> findAccount(String idAccount);

    Page<Product> findAllProduct(Pageable pageable);

    List<Product> findByStatus(String status);

    Page<Product> findByName(String productName, Pageable pageable);

    List<Product> findByCategoryAndNameProduct(String status, Integer idCategory, String nameProduct);

    List<Product> findByCategory(String status, Integer idCategory);

    List<Product> findByApproved(String status);

    List<Product> findAllByNotApprovedYet(String status, String idAccount);

    Page<Product> findAllProductByNotApprovedYet(Pageable pageable, String status, String idAccount);

    List<Product> findByNotApprovedYet(String status, String idAccount, String nameProduct);

    List<Product> findByNameProduct(String idAccount, String nameProduct);
}
