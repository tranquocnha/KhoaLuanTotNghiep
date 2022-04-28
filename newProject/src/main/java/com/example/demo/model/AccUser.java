package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;

import javax.persistence.*;
import java.util.Set;

@Entity
public class AccUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private int idUser;
    private String name;
    private boolean sex;
    private String dateOfBirth;
    @NotNull
    @JsonIgnore
    private String gmail;
    @JsonIgnore
    private int numberCard;
    @JsonIgnore
    private String phoneUser;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "account", referencedColumnName = "idAccount")
    @JsonIgnore
    private Account account;

    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JsonIgnore
    private Set<Bill> bills;

    @OneToMany(mappedBy = "accUser")
    @JsonIgnore
    private Set<CommentProduct> commentProducts;

    @OneToMany(mappedBy = "accUser",fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<Address> address;

    @OneToMany(mappedBy = "users")
    @JsonIgnore
    private Set<AuctionUser> auctionUsers;

    @OneToMany(mappedBy = "accUsers")
    @JsonIgnore
    private Set<MessageRecorderEntity> messageRecorderEntitySet;

    @OneToMany(mappedBy = "accUsers")
    @JsonIgnore
    private Set<CommentAccUser> commentAccUsers;

    public AccUser() {
    }

    public AccUser(String name, boolean sex, String dateOfBirth, String gmail, int numberCard, String phoneUser, Account account) {
        this.name = name;
        this.sex = sex;
        this.dateOfBirth = dateOfBirth;
        this.gmail = gmail;
        this.numberCard = numberCard;
        this.phoneUser = phoneUser;
        this.account = account;
    }


    public Set<CommentAccUser> getCommentAccUsers() {
        return commentAccUsers;
    }

    public void setCommentAccUsers(Set<CommentAccUser> commentAccUsers) {
        this.commentAccUsers = commentAccUsers;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGmail() {
        return gmail;
    }

    public void setGmail(String gmail) {
        this.gmail = gmail;
    }

    public int getNumberCard() {
        return numberCard;
    }

    public void setNumberCard(int numberCard) {
        this.numberCard = numberCard;
    }

    public String getPhoneUser() {
        return phoneUser;
    }

    public void setPhoneUser(String phoneUser) {
        this.phoneUser = phoneUser;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Set<Bill> getBills() {
        return bills;
    }

    public void setBills(Set<Bill> bills) {
        this.bills = bills;
    }

    public Set<CommentProduct> getCommentProducts() {
        return commentProducts;
    }

    public void setCommentProducts(Set<CommentProduct> commentProducts) {
        this.commentProducts = commentProducts;
    }

    public Set<MessageRecorderEntity> getMessageRecorderEntitySet() {
        return messageRecorderEntitySet;
    }

    public void setMessageRecorderEntitySet(Set<MessageRecorderEntity> messageRecorderEntitySet) {
        this.messageRecorderEntitySet = messageRecorderEntitySet;
    }

    public Set<Address> getAddress() {
        return address;
    }

    public void setAddress(Set<Address> address) {
        this.address = address;
    }

    public Set<AuctionUser> getAuctionUsers() {
        return auctionUsers;
    }

    public void setAuctionUsers(Set<AuctionUser> auctionUsers) {
        this.auctionUsers = auctionUsers;
    }
}
