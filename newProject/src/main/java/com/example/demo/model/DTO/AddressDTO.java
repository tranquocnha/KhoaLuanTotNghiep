package com.example.demo.model.DTO;


import com.example.demo.model.AccUser;
import com.example.demo.model.Ward;

public class AddressDTO {
    private String address;
    private String idWard;

    public AddressDTO() {
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
}
