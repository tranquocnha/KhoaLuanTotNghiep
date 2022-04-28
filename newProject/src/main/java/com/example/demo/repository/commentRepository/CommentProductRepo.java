package com.example.demo.repository.commentRepository;

import com.example.demo.model.CommentProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentProductRepo extends JpaRepository<CommentProduct,Integer> {
}
