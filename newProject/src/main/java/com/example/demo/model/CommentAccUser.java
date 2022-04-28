package com.example.demo.model;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
public class CommentAccUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idComment")
    private int idCommentAccUser;

    @ManyToOne(targetEntity = CommentRep.class)
    @JoinColumn(name = "id_comment_rep",referencedColumnName = "id_comment_rep")
    @JsonIgnore
    private CommentRep commentRep;

    @ManyToOne(targetEntity = AccUser.class)
    @JoinColumn(name = "id_user",referencedColumnName = "idUser")
    private AccUser accUsers;

    public CommentAccUser() {
    }

    public int getIdCommentAccUser() {
        return idCommentAccUser;
    }

    public void setIdCommentAccUser(int idCommentAccUser) {
        this.idCommentAccUser = idCommentAccUser;
    }

    public CommentRep getCommentRep() {
        return commentRep;
    }

    public void setCommentRep(CommentRep commentRep) {
        this.commentRep = commentRep;
    }

    public AccUser getAccUsers() {
        return accUsers;
    }

    public void setAccUsers(AccUser accUsers) {
        this.accUsers = accUsers;
    }
}
