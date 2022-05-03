package com.example.demo.service.socket;

import com.example.demo.model.MessageRecorderEntity;

import java.util.List;

public interface MessageRecorderService {

    void save(String sender, String receiver, String messageContent);

    List<MessageRecorderEntity> findAllByReceiverName(String receiverName);
}
