package com.example.demo.service.socket.impl;

import com.example.demo.model.MessageRecorderEntity;
import com.example.demo.repository.socket.MessageRecorderRepository;
import com.example.demo.service.socket.MessageRecorderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageRecorderServiceImpl implements MessageRecorderService {
    private final MessageRecorderRepository messageRecorderRepo;

    @Autowired
    public MessageRecorderServiceImpl(MessageRecorderRepository messageRecorderRepo) {
        this.messageRecorderRepo = messageRecorderRepo;
    }

    @Override
    public void save(String sender, String receiver, String messageContent) {
        MessageRecorderEntity messageEntity = new MessageRecorderEntity();
        messageEntity.setSenderName(sender);
        messageEntity.setReceiverName(receiver);
        messageEntity.setMessageContent(messageContent);
        messageRecorderRepo.save(messageEntity);
    }

    @Override
    public List<MessageRecorderEntity> findAllByReceiverName(String receiverName) {
        return messageRecorderRepo.findAllByReceiverName(receiverName);
    }
}
