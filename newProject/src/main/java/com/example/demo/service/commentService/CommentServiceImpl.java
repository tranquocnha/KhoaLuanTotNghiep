package com.example.demo.service.commentService;

import com.example.demo.model.Comment;
import com.example.demo.repository.commentRepository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService{
    @Autowired
    CommentRepository commentRepository;

    @Override
    public Page<Comment> findByProduct(int idProduct, Pageable pageable) {
        return commentRepository.findByProduct_IdProduct(idProduct,pageable);
    }

    @Override
    public List<Comment> findAll() {
        return commentRepository.findAll();
    }

    @Override
    public Comment findById(int idComment) {
        return commentRepository.findById(idComment).orElse(null);
    }

    @Override
    public void save(Comment comment) {
        commentRepository.save(comment);
    }

    @Override
    public void delete(int idComment) {
        commentRepository.deleteById(idComment);
    }
}
