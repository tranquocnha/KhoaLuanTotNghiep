package com.example.demo.repository.commentRepository;

import com.example.demo.model.CommentRep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepRepository extends JpaRepository<CommentRep,Integer> {
}
