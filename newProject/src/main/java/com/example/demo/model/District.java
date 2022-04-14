package com.example.demo.model;

import javax.persistence.*;
import java.util.Set;

@Entity
public class District {
    @Id
    private int districtId;
    private String districtName;

    @ManyToOne(targetEntity = Province.class)
    @JoinColumn(name = "province_id",referencedColumnName = "provinceId")
    private Province province;

    @OneToMany(mappedBy = "district")
    private Set<Ward> wards;

    public District() {
    }

    public Set<Ward> getWards() {
        return wards;
    }

    public void setWards(Set<Ward> wards) {
        this.wards = wards;
    }

    public int getDistrictId() {
        return districtId;
    }

    public void setDistrictId(int districtId) {
        this.districtId = districtId;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public Province getProvince() {
        return province;
    }

    public void setProvince(Province province) {
        this.province = province;
    }
}
