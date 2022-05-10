package com.example.demo.controller;

import com.example.demo.model.AccUser;
import com.example.demo.model.Discount;
import com.example.demo.model.DiscountCode;
import com.example.demo.model.DiscountStatus;
import com.example.demo.repository.UserRepository.UserRepository;
import com.example.demo.repository.discount.DiscountRepository;
import com.example.demo.service.discountService.DiscountCodeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Controller
public class DiscountController {
    @Autowired
    UserRepository userRepo;
    @Autowired
    DiscountCodeServiceImpl discountCodeService;
    @Autowired
    private DiscountRepository discountRepository;

    @GetMapping("/discount")
    public String createDiscount(Model model) {
        model.addAttribute("discount", new Discount());
        return "Hau/discount";

    }

    @PostMapping("/discount")
    public String getDiscount(@ModelAttribute Discount discount, Principal principal, Model model,RedirectAttributes redirectAttributes) {
        AccUser accUser = userRepo.findByAccount_IdAccount(principal.getName());
        int status=1;
        int str = discount.getPercent();
        int idAcount=accUser.getIdUser();
        List<String> discountCodeList = new ArrayList<>();
        discount.setAccounts(accUser.getAccount());
        discountRepository.save(discount);
        System.out.println(discount.toString());
        for (int i = 1; i <= discount.getQuantity(); i++) {
            int randomNumber = new Random().nextInt(9000) + 1000;
            String done = "KH" + "-" + idAcount + "-" + randomNumber + "-" + str;
            DiscountCode discountCode = new DiscountCode();
            discountCodeList.add(done);
            discountCode.setCode(done);
            // thang ban
//            discountCode.setAccUser(accUser);
            //
            discountCode.setDiscount(discount);
            discountCode.setDiscountStatus(new DiscountStatus(1,"Available"));
            discountCodeService.save(discountCode);
            System.out.println(discountCode);
        }
        model.addAttribute("discountCodeList", discountCodeList);
        model.addAttribute("idAcount", idAcount);
        model.addAttribute("discountCode",new DiscountCode());
        redirectAttributes.addFlashAttribute("mgss","them discount thanh cong");
        return "redirect:/product/list";
    }
    @GetMapping("/listDiscount")
    public String showList( Model model,Principal principal){
        AccUser accUser = userRepo.findByAccount_IdAccount(principal.getName());
//        discountRepository.
        return "Hau/code";
    }
    @GetMapping("bill/checkDiscount")
    public String checkDiscount(@RequestParam(name = "discountCode") String discountCode,
             @RequestParam(name = "total2") String total2,
//             @RequestParam(name = "totalPrice") String totalPrice,
                                RedirectAttributes redirectAttributes){
        System.out.println(discountCode);
        System.out.println("day la tong tien");
        String idSaler=discountCode.substring(3,5);
        redirectAttributes.addFlashAttribute("msgCheck","mã giảm giá hợp lệ");
        System.out.println(total2);
//        System.out.println(totalPrice);
        String discounts=discountCode.substring(11);
        System.out.println(discounts);
        return "redirect:/showCart";

    }


}
