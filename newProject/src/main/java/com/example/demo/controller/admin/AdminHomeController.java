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
@RequestMapping("/admin")
public class AdminHomeController {
    @Autowired
    private BillRepository billRepository;

    @GetMapping("")
    public String adminHome(Model model){

        if(billRepository.sumProductYear() !=null && Double.parseDouble(billRepository.sumProductYear())>=0 &&
                billRepository.sumAuctionYear()!=null && Double.parseDouble(billRepository.sumAuctionYear())>=0) {
            double sum = (Double.parseDouble(billRepository.sumProductYear()) + Double.parseDouble(billRepository.sumAuctionYear()));
            double product = Double.parseDouble(billRepository.sumProductYear());
            double auction = Double.parseDouble(billRepository.sumAuctionYear());
            double percentProduct = (product / sum) * 100;
            double percentAuction = (auction / sum) * 100;
            if (percentProduct >= 0) {
                model.addAttribute("percentProduct", percentProduct);
            }
            if (percentAuction >= 0) {
                model.addAttribute("percentAuction", percentAuction);
            }
        }
        model.addAttribute("sumTotal",billRepository.sumTotal());
        model.addAttribute("dateNow", LocalDate.now());
        model.addAttribute("sumYear2022",billRepository.sumTotalYear2022());
        model.addAttribute("sumQuantity",billRepository.sumQuantity());
        model.addAttribute("countAccount",billRepository.countAccount());
        model.addAttribute("countAccountSaler",billRepository.countAccountSaler());
        model.addAttribute("countAccountUser",billRepository.countAccountUser());
        model.addAttribute("countAuctionProduct",billRepository.countAuctionProduct());
        model.addAttribute("countProduct",String.valueOf(Integer.parseInt(billRepository.countProduct())-Integer.parseInt(billRepository.countAuctionProduct())));
        model.addAttribute("countComment",billRepository.countComment());



        return "/nha/revenue/Revenue";
    }
}
