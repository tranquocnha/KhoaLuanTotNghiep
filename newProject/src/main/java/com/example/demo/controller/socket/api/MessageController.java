package com.example.demo.controller.socket.api;

import com.example.demo.model.AccUser;
import com.example.demo.model.MessageEntity;
import com.example.demo.model.MessageRecorderEntity;
import com.example.demo.service.socket.AccUserService;
import com.example.demo.service.socket.MessageRecorderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
public class MessageController {
    @Autowired
    private AccUserService userServiceInterface;
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    private MessageRecorderService messageRecorder;
    @MessageMapping("/chat/{to}")
    public void sendMessage(@DestinationVariable String to, MessageEntity message) {
        AccUser destination = userServiceInterface.findByUserName(to);
        AccUser sender = userServiceInterface.findByUserName(message.getFromLogin());
        System.out.println("hello");
        int control = 0;
        if (destination != null ) {
            try {
                Boolean blockControl = userServiceInterface.blockControl(destination.getName(), sender.getName());
                if(blockControl.equals(Boolean.TRUE)) {
                    control=1;
                    throw new Exception("You can not sent message " + to);
                }
            }
            catch(Exception e) {
                if(control==0) {
                    simpMessagingTemplate.convertAndSend("/topic/messages/" + to, message);
                    messageRecorder.save(message.getFromLogin(), destination.getName(), message.getMessage());
                }
                control=1;
            }
            if(control==0) {
                simpMessagingTemplate.convertAndSend("/topic/messages/" + to, message);
                messageRecorder.save(message.getFromLogin(), destination.getName(), message.getMessage());
            }
        }
    }

    @GetMapping("/mymessages")
    public List<MessageRecorderEntity> getMyMessages(@RequestParam String receiverName) throws Exception{
        AccUser user = userServiceInterface.findByUserName(receiverName);
        if(user.getAccount().getIdAccount() == null) {
            throw new Exception("There is no user with this " + user.getName() + "user name!");
        }
        return messageRecorder.findAllByReceiverName(receiverName);

    }
}
