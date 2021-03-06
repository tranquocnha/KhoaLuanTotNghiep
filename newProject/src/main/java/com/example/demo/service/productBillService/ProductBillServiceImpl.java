package com.example.demo.service.productBillService;

import com.example.demo.model.Product;
import com.example.demo.model.ProductBill;
import com.example.demo.repository.productBillRepository.ProductBillRepository;
import com.example.demo.repository.productBillRepository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductBillServiceImpl implements ProductBillService{
    @Autowired
    ProductBillRepository productBillRepository;
    @Autowired
    ProductRepository productRepository;
    @Override
    public Product findProductByIdProduct(int id) {
        return productRepository.findProductByIdProduct(id);
    }

    @Override
    public void delete(int idProductBill) {
        productBillRepository.deleteById(idProductBill);
    }

    @Override
    public Page<ProductBill> findAll(Pageable pageable) {
        return productBillRepository.findAll(pageable);
    }

    @Override
    public Page<ProductBill> findByProduct_ProductNameContains(String nameProduct, Pageable pageable) {
        return productBillRepository.findByProduct_ProductNameContains(nameProduct , pageable);
    }

    @Override
    public Page<ProductBill> findByProduct_ProductNameAndBill_idAccountContains(String idAccount, String nameProducts, Pageable pageableSort) {
        return productBillRepository.findByProduct_ProductNameAndBill_User_NameContains(idAccount , nameProducts , pageableSort);
    }

    @Override
    public Page<ProductBill> findByBill_idAccountContains(String idAccount, Pageable pageableSort) {
        return productBillRepository.findByBill_User_NameContains(idAccount , pageableSort);
    }
}
