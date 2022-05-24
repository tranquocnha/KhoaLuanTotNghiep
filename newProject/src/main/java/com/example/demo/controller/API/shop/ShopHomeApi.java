package com.example.demo.controller.API.shop;

import com.example.demo.repository.productBillRepository.BillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/product/api")
public class ShopHomeApi {
    @Autowired
    private BillRepository billRepository;

    @GetMapping("/chart/year")
    public ResponseEntity<List<String>> charYear(){
        try{
            List<String> chartYear = billRepository.chartYearShop();
            return new ResponseEntity<>(chartYear, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
