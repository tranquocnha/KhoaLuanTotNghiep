package com.example.demo.service.productBillService;

import com.example.demo.model.Bill;
import com.example.demo.model.ProductBill;
import com.example.demo.repository.productBillRepository.BillRepository;
import com.example.demo.repository.productBillRepository.ProductBillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BillServiceImpl implements BillService{
    @Autowired
    private BillRepository billRepository;

    @Autowired
    private ProductBillRepository productBillRepository;

    @Override
    public List<Bill> findBills(int id) {
        return billRepository.findBills(id);
    }

    @Override
    public Bill findBillsById(int id) {
        return billRepository.findBillByIdBill(id);
    }

    @Override
    public Page<Bill> findBillAuction(String idAuction,Pageable pageable) {
        return billRepository.findBillAndTempAuction(idAuction,pageable);
    }

    @Override
    public Page<Bill> findAll(Pageable pageable) {
        return billRepository.findAll(pageable);
    }


    @Override
    public void delete(int id) {
        billRepository.deleteById(id);
    }

    @Override
    public void save(Bill bill) {
        billRepository.save(bill);
    }

    @Override
    public void saveDetail(ProductBill productBill) {
        productBillRepository.save(productBill);
    }
}
