package com.example.demo.repository.productBillRepository;

import com.example.demo.model.Bill;
import com.example.demo.model.DTO.ChartDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BillRepository extends JpaRepository<Bill, Integer> {
    Bill findBillByIdBill(int id);


    @Query(value = "select * " +
            "from bill " +
            "where id_user = :id and (status like 'Đang giao' or status like 'Đã giao') ;"
            , nativeQuery = true)
    List<Bill> findBills(@Param("id") int id);
    @Query(value = "select sum(b.total_cost) " +
            "from bill as b " +
            "where b.status like 'Đã giao' ;"
            ,nativeQuery = true)
    String sumTotal();
    @Query(value = "select sum(b.quantity) " +
            "from bill as b " +
            "where b.status like 'Đã giao' ;"
            ,nativeQuery = true)
    String sumQuantity();

    @Query(value = "select count(au.id_user) " +
            "from acc_user as au inner join account a on au.account = a.id_account " +
            "where a.status = true;"
            ,nativeQuery = true)
    String countAccount();
    @Query(value = "select count(au.id_user) " +
            "from acc_user as au " +
            "    inner join account a on au.account = a.id_account " +
            "    inner join account_role ar on a.id_account = ar.id_account " +
            "where a.status = true and ar.id_role = 3;"
            ,nativeQuery = true)
    String countAccountSaler();
    @Query(value = "select count(au.id_user)\n" +
            "from acc_user as au\n" +
            "         inner join account a on au.account = a.id_account\n" +
            "         inner join account_role ar on a.id_account = ar.id_account\n" +
            "where a.status = true and ar.id_role = 2;"
            ,nativeQuery = true)
    String countAccountUser();
    @Query(value = "select count(p.id_product)\n" +
            "from product as p\n" +
            "    inner join auction a on p.id_product = a.product_id_product\n" +
            "where p.status like 'Đã duyệt'"
            ,nativeQuery = true)
    String countAuctionProduct();
    @Query(value = "select count(p.id_product)\n" +
            "from product as p where p.status like 'Đã duyệt';"
            ,nativeQuery = true)
    String countProduct();
    @Query(value = "select count(cp.id_comment) " +
            "from comment as c " +
            "    inner join comment_product cp on c.id_comment = cp.id_comment;"
            ,nativeQuery = true)
    String countComment();

    @Query(value = "select year(b.current) as year,sum(b.total_cost) as total\n" +
            "from bill as b\n" +
            "    inner join temp_bill_product pb on b.id_bill = pb.id_bill\n" +
            "    inner join temp_product tp on pb.id_product = tp.id_product\n" +
            "where b.status like 'Đã giao'\n" +
            "group by year(b.current)\n" +
            "order by year(b.current) desc ;"
            ,nativeQuery = true)
    List<String> chartYear();
    @Query(value = "select month(b.current),sum(b.total_cost)\n" +
            "from bill as b\n" +
            "         inner join temp_auction ta on b.id_bill = ta.id_bill\n" +
            "where b.status like 'Đã giao'  and  year(b.current) like year(curdate())\n" +
            "group by month(b.current) order by month(b.current) desc;"
            ,nativeQuery = true)
    List<String> chartMothAuction();

    @Query(value = "select month(b.current) as month,sum(b.total_cost) as total\n" +
            "from bill as b\n" +
            "         inner join temp_bill_product pb on b.id_bill = pb.id_bill\n" +
            "         inner join temp_product tp on pb.id_product = tp.id_product\n" +
            "where b.status like 'Đã giao'  and  year(b.current) like year(curdate())\n" +
            "group by month(b.current) order by month(b.current) desc;"
            ,nativeQuery = true)
    List<String> chartMothProduct();

    @Query(value = "select year(b.current),sum(b.total_cost)\n" +
            "from bill as b\n" +
            "         inner join temp_auction ta on b.id_bill = ta.id_bill\n" +
            "where b.status like 'Đã giao'\n" +
            "group by year(b.current) order by year(b.current)  desc"
            ,nativeQuery = true)
    List<String> chartYearAuction();

    @Query(value = "select sum(b.total_cost)\n" +
            "from bill as b\n" +
            "         inner join temp_auction ta on b.id_bill = ta.id_bill\n" +
            "where b.status like 'Đã giao' and  year(b.current) like year(curdate());"
            ,nativeQuery = true)
    String sumTotalYear2022();

    @Query(value = "select sum(b.total_cost) as total\n" +
            "from bill as b\n" +
            "         inner join temp_bill_product pb on b.id_bill = pb.id_bill\n" +
            "         inner join temp_product tp on pb.id_product = tp.id_product\n" +
            "where b.status like 'Đã giao'"
            ,nativeQuery = true)
    String sumProductYear();

    @Query(value = "select sum(b.total_cost)\n" +
            "from bill as b\n" +
            "         inner join temp_auction ta on b.id_bill = ta.id_bill\n" +
            "where b.status like 'Đã giao';"
            ,nativeQuery = true)
    String sumAuctionYear();

    @Query(value = "select b.* from bill as b\n" +
            "    inner join temp_auction ta on b.id_bill = ta.id_bill\n" +
            "    inner join acc_user au on b.id_user = au.id_user\n" +
            "    inner join account a on au.account = a.id_account\n" +
            "where a.id_account like ?1 ",nativeQuery = true)
    Page<Bill> findBillAndTempAuction(String idAuction, Pageable pageable);

    // shop

    @Query(value = "select year(b.current) as year,sum(b.total_cost) as total\n" +
            "from bill as b\n" +
            "    inner join temp_bill_product pb on b.id_bill = pb.id_bill\n" +
            "    inner join temp_product tp on pb.id_product = tp.id_product\n" +
            "where b.status like 'Đã giao'\n" +
            "group by year(b.current) ;"
            ,nativeQuery = true)
    List<String> chartYearShop();


}
