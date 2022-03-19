package com.example.demo.repository.socket;

import com.example.demo.model.MessageRecorderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRecorderRepository extends JpaRepository<MessageRecorderEntity, Long> {
    List<MessageRecorderEntity> findAllByReceiverName(String receiverName);
}