package com.example.demo.repository.productBillRepository;

import com.example.demo.model.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BillRepository extends JpaRepository<Bill, Integer> {
    @Query(value = "select * from bill where id_user = :id ;", nativeQuery = true)
    List<Bill> findBills(@Param("id") int id);

    @Query("select sum(b.totalCost) from Bill b where  b.user.idUser=?1")
    String findRevenue(int id);
}
