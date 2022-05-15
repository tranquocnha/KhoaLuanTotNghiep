package com.example.demo.controller.API;


import com.example.demo.model.DTO.ChartDTO;
import com.example.demo.repository.productBillRepository.BillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/admin/api")
public class AdminHomeAPI {
    @Autowired
    private BillRepository billRepository;
    @GetMapping("/chart/year")
    public  ResponseEntity<List<String>> charYear(){
        try{
            List<String> chartYear = billRepository.chartYear();
            return new ResponseEntity<>(chartYear, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/chart/year/auction")
    public  ResponseEntity<List<String>> charYearAuction(){
        try{
            List<String> chartYearAuction = billRepository.chartYearAuction();
            return new ResponseEntity<>(chartYearAuction, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/chart/month/auction")
    public  ResponseEntity<List<String>> charMonthAuction(){
        try{
            List<String> chartYearAuction = billRepository.chartMothAuction();
            return new ResponseEntity<>(chartYearAuction, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/chart/month/product")
    public  ResponseEntity<List<String>> charYearProduct(){
        try{
            List<String> chartYearAuction = billRepository.chartMothProduct();
            return new ResponseEntity<>(chartYearAuction, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
