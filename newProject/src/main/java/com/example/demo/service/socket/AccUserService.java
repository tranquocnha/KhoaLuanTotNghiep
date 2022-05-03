package com.example.demo.service.socket;

import com.example.demo.model.AccUser;

import java.util.Set;

public interface AccUserService {
    AccUser findByEmail(String mail);

    AccUser findByUserName(String userName);

    Set<String> findAllByName();

    Boolean blockControl(String name, String name1);
}
