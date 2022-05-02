package com.example.demo.service.socket;

import com.example.demo.model.AccUser;

import java.util.List;
import java.util.Set;

public interface AccUserService {
    AccUser findByEmail(String mail);

    AccUser findByUserName(String userName);

    List<AccUser> findAllByName();

    Boolean blockControl(String name, String name1);
}
