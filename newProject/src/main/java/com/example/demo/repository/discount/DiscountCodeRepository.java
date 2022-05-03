package com.example.demo.repository.discount;

import com.example.demo.model.Discount;
import com.example.demo.model.DiscountCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface DiscountCodeRepository extends JpaRepository<DiscountCode, Integer> {
    List<DiscountCode> findDiscountCodeByDiscount_NameDiscount(String idDiscount);

    List<DiscountCode> findDiscountCodeByDiscount_NameDiscountAndDiscountStatus_IdStatus(String idDiscount, int statusId);

//    @Modifying
//    @Query(value = "insert into discount d VALUES(d.id_code=:id_code,d.id_discount_code=:id,d.id_status=1)", nativeQuery = true)
//    void insertCode(@Param("id") int id, @Param("id_code") int id_code);
//    @Query(value = "insert into Logger t (t.redirect, t.user.id) VALUES (?#{principal.id},)", nativeQuery = true)

//    @Query(value = "select 'code' from DiscountCode dc inner join Discount d on d.idDiscount=dc.discount inner join Account a on a.idAccount=d.accounts inner join AccUser ac on ac.account=a.idAccount where ac.idUser=:id")


    @Query(value = "select * from discount_code dc inner join discount on discount.id_discount=dc.id_discount inner join `account` on `account`.id_account=discount.`account` inner join acc_user on acc_user.`account`=`account`.id_account where acc_user.id_user=:id", nativeQuery = true)

    List<DiscountCode> findDiscountCodeByIdSaler(int id);
}
