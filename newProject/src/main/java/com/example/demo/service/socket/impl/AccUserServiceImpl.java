package com.example.demo.service.socket.impl;

import com.example.demo.model.AccUser;
import com.example.demo.model.BlockUserEntity;
import com.example.demo.repository.socket.BlockUserRepository;
import com.example.demo.repository.socket.AccUserRepository;
import com.example.demo.service.socket.AccUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AccUserServiceImpl implements AccUserService {
    @Autowired
    private AccUserRepository accUserRepository;
    @Autowired
    private BlockUserRepository blockUserRepository;
    @Override
    public AccUser findByEmail(String mail) {
        return accUserRepository.findByGmail(mail);
    }

    @Override
    public AccUser findByUserName(String userName) {
        return accUserRepository.findByAccount_IdAccount(userName);
    }

    @Override
    public List<AccUser> findAllByName() {
        return  accUserRepository.findAll();
    }

    @Override
    public Boolean blockControl(String angryName, String blockedName) {
        AccUser angry = accUserRepository.findByName(angryName);
        AccUser blocked = accUserRepository.findByName(blockedName);
        List<BlockUserEntity> listOfBlock = blockUserRepository.findAllByAngryId((long) angry.getIdUser());
        ArrayList<Long> blockedIds = new ArrayList<Long>();

        for (BlockUserEntity blockUserEntity : listOfBlock) {
            blockedIds.add(blockUserEntity.getBlockedId());
        }
        return blockedIds.contains((long) blocked.getIdUser());
    }
}
