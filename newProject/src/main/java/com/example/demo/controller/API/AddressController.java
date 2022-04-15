package com.example.demo.controller.API;

import com.example.demo.model.District;
import com.example.demo.model.Province;
import com.example.demo.model.Ward;
import com.example.demo.repository.addressRepository.DistrictRepository;
import com.example.demo.repository.addressRepository.ProvinceRepository;
import com.example.demo.repository.addressRepository.WardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/address")
public class AddressController {
    private final WardRepository wardRepository;
    private final ProvinceRepository provinceRepository;
    private final DistrictRepository districtRepository;

    @Autowired
    public AddressController(WardRepository wardRepository,ProvinceRepository provinceRepository,DistrictRepository districtRepository){
        this.wardRepository = wardRepository;
        this.provinceRepository = provinceRepository;
        this.districtRepository = districtRepository;
    }
    @GetMapping("/province")
    public ResponseEntity<Iterable<Province>> listProvinces() {
        try{
            Iterable<Province> provinces = provinceRepository.findAllProvince();
            return new ResponseEntity<>(provinces, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/district/{id}")
    public ResponseEntity<Iterable<District>> listDistrictByProvinceId(@PathVariable("id") int provinceId) {
        try{
            Iterable<District> districts = districtRepository.findAllByProvinceId(provinceId);
            return new ResponseEntity<>(districts, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/ward/{id}")
    public ResponseEntity<Iterable<Ward>> listWardByDistrictId(@PathVariable("id") int districtId) {
        try{
            Iterable<Ward> wards = wardRepository.findAllByDistrictId(districtId);
            return new ResponseEntity<>(wards, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
