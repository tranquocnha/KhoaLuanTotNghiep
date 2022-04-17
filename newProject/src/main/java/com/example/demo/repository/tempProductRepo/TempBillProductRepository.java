package com.example.demo.repository.tempProductRepo;

import com.example.demo.model.TempBillProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TempBillProductRepository extends JpaRepository<TempBillProduct, Integer> {
}
