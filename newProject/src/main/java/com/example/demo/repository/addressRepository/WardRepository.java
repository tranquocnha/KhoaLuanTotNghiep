package com.example.demo.repository.addressRepository;

import com.example.demo.model.Ward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface WardRepository extends JpaRepository<Ward, Integer> {
    @Query(value = "SELECT b FROM Ward b WHERE b.district.districtId = ?1")
    Iterable<Ward> findAllByDistrictId(int districtId);
}
