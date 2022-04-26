package com.example.demo.service.productBillService;

import com.example.demo.model.Bill;
import com.example.demo.model.ProductBill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BillService {
    List<Bill> findBills(int id);

    Page<Bill> findAll(Pageable pageable);

    void delete(int id);

    void save(Bill bill);

    void saveDetail(ProductBill productBill);
}
