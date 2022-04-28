package com.example.demo.controller.API;

import com.example.demo.model.AccUser;
import com.example.demo.model.Comment;
import com.example.demo.model.CommentProduct;
import com.example.demo.model.DTO.CommentDTO;
import com.example.demo.model.Product;
import com.example.demo.repository.commentRepository.CommentProductRepo;
import com.example.demo.repository.commentRepository.CommentRepository;
import com.example.demo.repository.productBillRepository.ProductRepository;
import com.example.demo.repository.socket.AccUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api")
public class CommentController {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private AccUserRepository accUserRepository;
    @Autowired
    private CommentProductRepo commentProductRepo;
    @GetMapping("/comment/{id}")
    public ResponseEntity<Iterable<Comment>> commentProduct(@PathVariable("id")int id){
        try{
            List<Comment> comments = commentRepository.findByProduct_IdProduct(id);
            return new ResponseEntity<>(comments, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/comment")
    public ResponseEntity<CommentProduct> createComment(@RequestBody CommentDTO comment, Principal principal){
        try{
            System.out.println("content"+comment.getContent()+"idProduct"+comment.getIdProduct());
            Product product = productRepository.findById(comment.getIdProduct()).orElse(null);
            AccUser accUser = accUserRepository.findByAccount_IdAccount(principal.getName());
            Comment commentSave = new Comment();
            commentSave.setContent(comment.getContent());
            commentSave.setProduct(product);
            commentRepository.save(commentSave);
            CommentProduct commentProduct = new CommentProduct();
            commentProduct.setComment(commentSave);
            commentProduct.setAccUser(accUser);
            commentProductRepo.save(commentProduct);
            return new ResponseEntity<>(commentProduct, HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }



}
