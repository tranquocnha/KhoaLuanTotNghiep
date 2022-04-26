package com.example.demo.controller.admin;


import com.example.demo.repository.productBillRepository.BillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/adminhome")
public class AdminHomeController {
    @Autowired
    private BillRepository billRepository;

    @GetMapping("")
    public String adminHome(Model model){
        String sumTotal = billRepository.sumTotal();

        model.addAttribute("sumTotal",sumTotal);
        model.addAttribute("dateNow", LocalDate.now());
        return "/nha/revenue/Revenue";
    }
}
