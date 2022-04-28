package com.example.demo.repository.commentRepository;

import com.example.demo.model.CommentAccUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentAccUserRepository extends JpaRepository<CommentAccUser,Integer> {
}
