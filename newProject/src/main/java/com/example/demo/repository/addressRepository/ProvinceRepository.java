package com.example.demo.repository.addressRepository;

import com.example.demo.model.District;
import com.example.demo.model.Province;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProvinceRepository extends JpaRepository<Province,Integer> {
    @Query(value = "SELECT d FROM Province d ")
    Iterable<Province> findAllProvince();
}
