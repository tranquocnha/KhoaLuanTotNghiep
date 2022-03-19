package com.example.demo.repository.accountRepository;

import com.example.demo.model.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface AccountRepository extends JpaRepository<Account, String> {
    Account findByIdAccount(String account);

    @Query(value = "select `password` from `account`  where id_account =:idAccount", nativeQuery = true)
    String findByPassword(@Param("idAccount") String idAccount);

//    @Query(value = "select pass_word from account where user_name =:userName", nativeQuery = true)
//    String  findByPass(@Param("userName") String userName);
//    String findByPassword(@Param("idAccount")String idAccount);

    @Query(value="select *  from  acc_user join`account`  where  `account`.id_account=acc_user.`account` and   gmail=:gmail", nativeQuery = true)

    Account findAccountByGmail(@Param("gmail")String gmail);


    Account findAccountByResetPasswordToken(String token);

}
