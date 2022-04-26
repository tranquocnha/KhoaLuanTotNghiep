package com.example.demo.controller;

import com.example.demo.model.AccUser;
import com.example.demo.model.Bill;
import com.example.demo.model.ProductBill;
import com.example.demo.model.TempBillProduct;
import com.example.demo.repository.UserRepository.UserRepository;
import com.example.demo.repository.tempProductRepo.TempBillProductRepository;
import com.example.demo.service.productBillService.BillService;
import com.example.demo.service.productBillService.ProductBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/nhas")
public class DonGiaController {
    private final ProductBillService productBillService;
    private final BillService billService;
    private final UserRepository userRepo;
    private final TempBillProductRepository tempBillProductRepository;

    @Autowired
    public DonGiaController(ProductBillService productBillService, BillService billService, UserRepository userRepo, TempBillProductRepository tempBillProductRepository) {
        this.productBillService = productBillService;
        this.billService = billService;
        this.userRepo = userRepo;
        this.tempBillProductRepository = tempBillProductRepository;
    }




    @ModelAttribute("userNames")
    public AccUser getDauGia() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userRepo.findByAccount_IdAccount(auth.getName());
    }
    @ModelAttribute("admin")
    public String AdminOrSaler(){
        Authentication  auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth.getAuthorities().toString().equals("[ROLE_ADMIN]")){
            if (SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"))) {
                return "là admin";
            }
        }else{
            if (SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().
                    anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_SALER"))) {
                return "là saler";
            }
        }
        return null;
    }
    @RequestMapping("/dongia")
    public ModelAndView listAll(@RequestParam(defaultValue = "0") int page){
        ModelAndView model = new ModelAndView("/nha/DonGia");
        Pageable pageable = PageRequest.of(page, 5);
        Page<Bill> productBillList = billService.findAll(pageable);
        model.addObject("listDonGia",productBillList);
        return model;
    }

    @GetMapping("/bill/{idBill}/view")
    public String viewBill(@PathVariable("idBill") int idBill,@RequestParam(defaultValue = "0") int page, Model model){
        Pageable pageable = PageRequest.of(page, 5);
        Page<TempBillProduct> tempBillProduct = tempBillProductRepository.findByBills_IdBill(idBill,pageable);
        model.addAttribute("viewBillProduct",tempBillProduct);
        model.addAttribute("idBill",idBill);
        return "/nha/bill/ViewBill";
    }

    @PostMapping("/pagaList")
    public ModelAndView getList(@RequestParam(defaultValue = "0") int page, @RequestParam String nameUser,
                                @RequestParam String nameProduct) {
        ModelAndView modelAndView = new ModelAndView("/nha/DonGia");
        Page<ProductBill> productBills;
        Pageable pageableSort = PageRequest.of(page, 999);
        if (nameUser.equals("")) {
            if (!nameProduct.equals("")) {
                productBills = productBillService.findByProduct_ProductNameContains(nameProduct, pageableSort);
            } else {
                productBills = productBillService.findAll(pageableSort);
            }
        } else {
            if (!nameProduct.equals("")) {
                productBills = productBillService.findByProduct_ProductNameAndBill_idAccountContains(nameUser, nameProduct, pageableSort);
            } else {
                productBills = productBillService.findByBill_idAccountContains(nameUser, pageableSort);
            }
        }
        modelAndView.addObject("nameUser", nameUser);
        modelAndView.addObject("productBills", productBills);
        modelAndView.addObject("nameProduct", nameProduct);
        return modelAndView;
    }

//    @GetMapping("/{id}/delete")
//    public String delete(@PathVariable("id") int id) {
//        productBillService.delete(id);
//        return "redirect:/nhas/dongia";
//    }

}
