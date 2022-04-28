package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Set;

@Entity
public class CommentRep {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_comment_rep")
    private int idCommentRep;

    @Column(length = 2000)
    private String Content;

    @ManyToOne(targetEntity = Comment.class)
    @JoinColumn(name = "id_comment", referencedColumnName = "idComment")
    @JsonIgnore
    private Comment comments;

    @OneToMany(mappedBy = "commentRep")
    private Set<CommentAccUser> commentAccUsers;

    public CommentRep() {
    }

    public int getIdCommentRep() {
        return idCommentRep;
    }

    public void setIdCommentRep(int idCommentRep) {
        this.idCommentRep = idCommentRep;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public Comment getComments() {
        return comments;
    }

    public void setComments(Comment comments) {
        this.comments = comments;
    }

    public Set<CommentAccUser> getCommentAccUsers() {
        return commentAccUsers;
    }

    public void setCommentAccUsers(Set<CommentAccUser> commentAccUsers) {
        this.commentAccUsers = commentAccUsers;
    }
}
