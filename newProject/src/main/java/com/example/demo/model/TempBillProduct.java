package com.example.demo.model;

import javax.persistence.*;

@Entity
public class TempBillProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idTempBillProduct;

    @ManyToOne(targetEntity = TempProduct.class)
    @JoinColumn(name = "idProduct",referencedColumnName = "idProduct")
    private TempProduct tempProduct;

    @ManyToOne(targetEntity = Bill.class)
    @JoinColumn(name = "idBill",referencedColumnName = "idBill")
    private Bill bills;

    public TempBillProduct() {
    }

    public TempBillProduct(int idTempBillProduct,TempProduct tempProduct, Bill bills) {
        this.idTempBillProduct = idTempBillProduct;
        this.tempProduct = tempProduct;
        this.bills = bills;
    }

    public int getIdTempBillProduct() {
        return idTempBillProduct;
    }

    public void setIdTempBillProduct(int idTempBillProduct) {
        this.idTempBillProduct = idTempBillProduct;
    }

    public TempProduct getTempProduct() {
        return tempProduct;
    }

    public void setTempProduct(TempProduct tempProduct) {
        this.tempProduct = tempProduct;
    }

    public Bill getBills() {
        return bills;
    }

    public void setBills(Bill bills) {
        this.bills = bills;
    }
}
