package com.example.demo.repository.commentRepository;

import com.example.demo.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    Page<Comment> findByProduct_IdProduct(int idProduct, Pageable pageable);
    List<Comment> findByProduct_IdProductOrderByIdCommentDesc(int idProduct);

}
