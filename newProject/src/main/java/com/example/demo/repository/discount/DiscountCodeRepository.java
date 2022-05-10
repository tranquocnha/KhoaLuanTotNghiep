package com.example.demo.repository.discount;

import com.example.demo.model.Discount;
import com.example.demo.model.DiscountCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DiscountCodeRepository extends JpaRepository<DiscountCode , Integer> {
    List<DiscountCode> findDiscountCodeByDiscount_NameDiscount(String idDiscount);

    List<DiscountCode> findDiscountCodeByDiscount_NameDiscountAndDiscountStatus_IdStatus(String idDiscount , int statusId);

    @Query(value = "select * from discount_code dc inner join discount on discount.id_discount=dc.id_discount inner join `account` on `account`.id_account=discount.`account` inner join acc_user on acc_user.`account`=`account`.id_account where acc_user.id_user=:id", nativeQuery = true)

    List<DiscountCode> findDiscountCodeByIdSaler(int id);
}
