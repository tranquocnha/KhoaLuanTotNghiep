package com.example.demo.service.product;

import com.example.demo.model.Product;
import com.example.demo.repository.productBillRepository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    ProductRepository productRepository;

    @Override
    public Page<Product> findProductAndComment(Pageable pageable) {
        return productRepository.findProductAndComment(pageable);
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public String findIdAccountByProduct(int id) {
        return productRepository.findIdAccountByProduct(id);
    }

    @Override
    public Product findProductByIdProduct(int id) {
        return productRepository.findProductByIdProduct(id);
    }

    @Override
    public void save(Product product) {
        productRepository.save(product);
    }

    @Override
    public Product findById(int id) {
        return productRepository.findById(id).orElse(null);
    }

    @Override
    public Product findByIdStatus(String status, int idProduct) {
        return productRepository.findByStatusAndIdProduct(status, idProduct);
    }

    @Override
    public void delete(int id) {
        productRepository.deleteById(id);
    }

    @Override
    public int findByQuantity(int id) {
        return productRepository.findByQuantity(id);
    }

    @Override
    public List<Product> findProduct(String idAccount) {
        return productRepository.findProduct(idAccount);
    }

    @Override
    public List<Product> findByNameApproved(String status, String nameProduct) {
        return productRepository.findByStatusAndProductNameContains(status, nameProduct);
    }

    @Override
    public List<Product> findByCategory(String status, int idCategory) {
        return productRepository.findByStatusAndCategory_IdCategoryOrderByPrice(status, idCategory);
    }

    @Override
    public List<Product> findAccount(String idAccount) {
        return productRepository.findAccount(idAccount);
    }

    @Override
    public Page<Product> findAllProduct(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Override
    public List<Product> findByStatus(String status) {
        return productRepository.findAllByStatusContaining(status);
    }

    @Override
    public Page<Product> findByName(String productName, Pageable pageable) {
        return productRepository.findByProductNameContains(productName, pageable);
    }

    @Override
    public List<Product> findByCategoryAndNameProduct(String status, Integer idCategory, String nameProduct) {
        return productRepository.findByStatusAndCategory_IdCategoryAndProductNameContainsOrderByPrice(status, idCategory, nameProduct);
    }

    @Override
    public List<Product> findByCategory(String status, Integer idCategory) {
        return productRepository.findByStatusAndCategory_IdCategoryOrderByPrice(status, idCategory);
    }

    @Override
    public List<Product> findByApproved(String status) {
        return productRepository.findAllApproved(status);
    }

    @Override
    public List<Product> findAllByNotApprovedYet(String status, String idAccount) {
        return productRepository.findAllByNotApprovedYet(status, idAccount);
    }

    @Override
    public Page<Product> findAllProductByNotApprovedYet(Pageable pageable, String status, String idAccount) {
        return productRepository.findAllProductByNotApprovedYet(pageable, status, idAccount);
    }

    @Override
    public List<Product> findByNotApprovedYet(String status, String idAccount, String nameProduct) {
        return productRepository.findByProductNameNotApprovedYet(status, idAccount, nameProduct);
    }

    @Override
    public List<Product> findByNameProduct(String idAccount, String nameProduct) {
        return productRepository.findByMyNameProduct(idAccount, nameProduct);
    }
}
