package com.example.demo.controller.API;

import com.example.demo.model.Bill;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/admin/api/")
public class AdminHomeAPI {

    @GetMapping("/chartyear")
    public  ResponseEntity<Iterable<Bill>> charYear(){
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
