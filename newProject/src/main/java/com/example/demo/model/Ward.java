package com.example.demo.model;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Ward {
    @Id
    private int wardId;
    private String wardName;

    @ManyToOne(targetEntity = District.class)
    @JoinColumn(name="district_id",referencedColumnName = "districtId")
    private District district;

    @OneToMany(mappedBy = "ward")
    private Set<Bill> bills;

    public Ward() {
    }

    public int getWardId() {
        return wardId;
    }

    public void setWardId(int wardId) {
        this.wardId = wardId;
    }

    public String getWardName() {
        return wardName;
    }

    public void setWardName(String wardName) {
        this.wardName = wardName;
    }

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    public Set<Bill> getBills() {
        return bills;
    }

    public void setBills(Set<Bill> bills) {
        this.bills = bills;
    }
}
