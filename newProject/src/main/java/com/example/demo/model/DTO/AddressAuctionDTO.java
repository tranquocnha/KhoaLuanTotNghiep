package com.example.demo.model.DTO;

public class AddressAuctionDTO {
    private String address;
    private String idWard;
    private int idProduct;
    private Double totalAuction;


    public AddressAuctionDTO() {
    }

    public Double getTotalAuction() {
        return totalAuction;
    }

    public void setTotalAuction(Double totalAuction) {
        this.totalAuction = totalAuction;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIdWard() {
        return idWard;
    }

    public void setIdWard(String idWard) {
        this.idWard = idWard;
    }

    public int getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }

    @Override
    public String toString() {
        return "AddressAuctionDTO{" +
                "address='" + address + '\'' +
                ", idWard='" + idWard + '\'' +
                ", idProduct=" + idProduct +
                ", totalAuction=" + totalAuction +
                '}';
    }
}
