package com.example.demo.service.commentService;

import com.example.demo.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommentService {
    Page<Comment> findByProduct(int idProduct, Pageable pageable);

    List<Comment> findAll();

    Comment findById(int idComment);

    void save(Comment comment);

    void delete(int idComment);
}
