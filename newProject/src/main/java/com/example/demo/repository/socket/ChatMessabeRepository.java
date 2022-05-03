package com.example.demo.repository.socket;

import com.example.demo.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessabeRepository extends JpaRepository<ChatMessage,Integer> {
}
