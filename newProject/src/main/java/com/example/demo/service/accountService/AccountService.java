package com.example.demo.service.accountService;

import com.example.demo.model.AccUser;
import com.example.demo.model.Account;

import java.util.List;

public interface AccountService {
    List<Account>  findAll();
    int findIdUserByIdAccount(String id);
    void save(Account account);
    AccUser findAccUserByIdUser(int id);
    Account findById(String idAccount);

    String findByPassword(String password);
}
