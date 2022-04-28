package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
public class CommentProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idCommentProduct;

    @ManyToOne
    @JoinColumn(name = "id_user",referencedColumnName = "idUser")
    private AccUser accUser;

    @ManyToOne
    @JoinColumn(name = "id_comment",referencedColumnName = "idComment")
    @JsonIgnore
    private Comment comment;

    public CommentProduct() {
    }

    public int getIdCommentProduct() {
        return idCommentProduct;
    }

    public void setIdCommentProduct(int idCommentProduct) {
        this.idCommentProduct = idCommentProduct;
    }

    public AccUser getAccUser() {
        return accUser;
    }

    public void setAccUser(AccUser accUser) {
        this.accUser = accUser;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }
}
