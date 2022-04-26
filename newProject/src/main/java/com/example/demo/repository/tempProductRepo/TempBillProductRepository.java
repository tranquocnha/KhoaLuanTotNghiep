package com.example.demo.repository.tempProductRepo;

import com.example.demo.model.TempBillProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TempBillProductRepository extends JpaRepository<TempBillProduct, Integer> {

    Page<TempBillProduct> findByBills_IdBill(int idBill,Pageable pageable);
}
