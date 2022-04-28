package com.example.demo.model.DTO;


import lombok.Data;

import javax.persistence.Column;

@Data
public class CommentRepDTO {
    private int idComment;

    @Column(length = 2000)
    private String content;

    public CommentRepDTO() {
    }

    public int getIdComment() {
        return idComment;
    }

    public void setIdComment(int idComment) {
        this.idComment = idComment;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
