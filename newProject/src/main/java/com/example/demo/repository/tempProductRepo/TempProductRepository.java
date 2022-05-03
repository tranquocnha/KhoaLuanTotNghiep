package com.example.demo.repository.tempProductRepo;

import com.example.demo.model.TempProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TempProductRepository extends JpaRepository<TempProduct,Integer> {
}
