package com.example.demo.repository.productBillRepository;

import com.example.demo.model.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BillRepository extends JpaRepository<Bill, Integer> {
    @Query(value = "select * from bill where id_user = :id ;", nativeQuery = true)
    List<Bill> findBills(@Param("id") int id);
    @Query(value = "select sum(b.total_cost) from bill as b where b.status like 'Đang giao' or b.status like 'Đã giao' ;",nativeQuery = true)
    String sumTotal();
}
