package com.example.demo.model;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idBill")
    private int idBill;
    private String current;
    private double totalCost;
    private int quantity;
    private String status;
    private String address;

    @ManyToOne(targetEntity = AccUser.class)
    @JoinColumn(name = "idUser", referencedColumnName = "idUser")
    private AccUser user;

    @OneToMany(mappedBy = "bill")
    private Set<ProductBill> productBills;

    @OneToMany(mappedBy = "bills")
    private Set<TempBillProduct> tempBillProducts;

    @ManyToOne(targetEntity = Ward.class)
    @JoinColumn(name ="ward_id",referencedColumnName = "wardId")
    private Ward ward;

    @OneToOne(mappedBy = "bliss")
    private TempAuction tempAuction;

    public Bill() {
    }

    public Bill(int idBill, String current, double totalCost, String status, AccUser user, Set<ProductBill> productBills) {
        this.idBill = idBill;
        this.current = current;
        this.totalCost = totalCost;
        this.status = status;
        this.user = user;
        this.productBills = productBills;
    }

    public TempAuction getTempAuction() {
        return tempAuction;
    }

    public void setTempAuction(TempAuction tempAuction) {
        this.tempAuction = tempAuction;
    }

    public Set<TempBillProduct> getTempBillProducts() {
        return tempBillProducts;
    }

    public void setTempBillProducts(Set<TempBillProduct> tempBillProducts) {
        this.tempBillProducts = tempBillProducts;
    }

    public String getAddress() {
        return address;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Ward getWard() {
        return ward;
    }

    public void setWard(Ward ward) {
        this.ward = ward;
    }

    public int getIdBill() {
        return idBill;
    }

    public void setIdBill(int idBill) {
        this.idBill = idBill;
    }

    public String getCurrent() {
        return current;
    }

    public void setCurrent(String current) {
        this.current = current;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public AccUser getUser() {
        return user;
    }

    public void setUser(AccUser user) {
        this.user = user;
    }

    public Set<ProductBill> getProductBills() {
        return productBills;
    }

    public void setProductBills(Set<ProductBill> productBills) {
        this.productBills = productBills;
    }
}
