package com.example.demo.service.accountService;

import com.example.demo.model.Account;
import com.example.demo.repository.accountRepository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountNotFoundException;
import java.util.List;

@Service
public class AccountServiceImpl implements AccountService{
    @Autowired
    AccountRepository accountRepository;

    @Override
    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    @Override
    public void save(Account account) {
        accountRepository.save(account);
    }

    @Override
    public Account findById(String idAccount) {
        return accountRepository.findById(idAccount).orElse(null);
    }

    @Override
    public String findByPassword(String password) {
        return accountRepository.findByPassword(password);
    }

    public void updateResetPasswordToken(String token, String email) throws AccountNotFoundException {
        Account account = accountRepository.findAccountByGmail(email);
//        Account account1 = accountRepo.findAccountByUser_Name(email);

        System.out.println("day la email  " + account);
        if (account != null) {
            account.setResetPasswordToken(token);
            accountRepository.save(account);
        } else {
            throw new AccountNotFoundException("Không tìm thấy account !" + email);

        }
    }

    public Account get(String resetPasswordToken) {
        return accountRepository.findAccountByResetPasswordToken(resetPasswordToken);
    }

    public void updatePassword(Account account, String newPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(newPassword);
        account.setResetPasswordToken(null);
        account.setPassword(encodedPassword);
        accountRepository.save(account);
    }
}
