package com.example.demo.repository.socket;

import com.example.demo.model.AccUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccUserRepository extends JpaRepository<AccUser, Long> {

    AccUser findByGmail(String mail);

    AccUser findByName(String name);
    AccUser findByAccount_IdAccount(String idAccount);
}
