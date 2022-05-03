package com.example.demo.repository.addressRepository;

import com.example.demo.model.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DistrictRepository extends JpaRepository<District, Integer> {
    @Query(value = "SELECT d FROM District d WHERE d.province.provinceId = ?1")
    Iterable<District> findAllByProvinceId(int provinceId);
}
