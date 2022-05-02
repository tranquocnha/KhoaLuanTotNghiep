package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idComment")
    private int idComment;

    @ManyToOne(targetEntity = Product.class)
    @JoinColumn(name = "idProduct", referencedColumnName = "idProduct")
    @JsonIgnore
    private Product product;

    @Column(length = 2000)
    private String Content;



    @OneToMany(mappedBy = "comment")
    private Set<CommentProduct> userSet;

    @OneToMany(mappedBy = "comments")
    private Set<CommentRep> commentReps;

    public Comment() {
    }

    public Comment(int idComment, Product product, String content, Set<CommentProduct> userSet) {
        this.idComment = idComment;
        this.product = product;
        Content = content;
        this.userSet = userSet;
    }

    public int getIdComment() {
        return idComment;
    }

    public void setIdComment(int idComment) {
        this.idComment = idComment;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public Set<CommentProduct> getUserSet() {
        return userSet;
    }

    public void setUserSet(Set<CommentProduct> userSet) {
        this.userSet = userSet;
    }

    public Set<CommentRep> getCommentReps() {
        return commentReps;
    }

    public void setCommentReps(Set<CommentRep> commentReps) {
        this.commentReps = commentReps;
    }
}
