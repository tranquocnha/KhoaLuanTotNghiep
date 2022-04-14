package com.example.demo.controller;

import com.example.demo.model.AccUser;
import com.example.demo.model.Color;
import com.example.demo.model.Product;
import com.example.demo.repository.UserRepository.UserRepository;
import com.example.demo.repository.categoryRepository.CategoryRepository;
import com.example.demo.repository.productBillRepository.ProductRepository;
import com.example.demo.service.colorService.ColorServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class SearchHomeController {
    private final UserRepository userRepo;
    private final ProductRepository productRepository;
    private final ColorServiceImpl colorService;
    private final CategoryRepository categoryRepository;

    @Autowired
    public SearchHomeController(UserRepository userRepo, ProductRepository productRepository, ColorServiceImpl colorService, CategoryRepository categoryRepository) {
        this.userRepo = userRepo;
        this.productRepository = productRepository;
        this.colorService = colorService;
        this.categoryRepository = categoryRepository;
    }

    @ModelAttribute("userNames")
    public AccUser getDauGia() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userRepo.findByAccount_IdAccount(auth.getName());
    }

    @ModelAttribute("admin")
    public String AdminOrSaler() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().toString().equals("[ROLE_ADMIN]")) {
            if (SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"))) {
                return "là admin";
            }
        } else {
            if (SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().
                    anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_SALER"))) {
                return "là saler";
            }
        }
        return null;
    }

    //    /*
//        findByCategory
//    **/
//    @GetMapping("/searchProduct/{idProduct}")
//    public String searchProduct(@PathVariable(name="idProduct")int id, Model model){
//
//        List<Color> colorList = colorService.findProduct("Đã duyệt",id);
//        model.addAttribute("drowdownCategory",categoryRepository.findAll());
//        model.addAttribute("category",categoryRepository.findTop());
//        model.addAttribute("idCategory",id);
//        model.addAttribute("product",colorList);
//        return "/nha/Search";
//
//    }
    @GetMapping("/searchProduct")
    public String searchProductAll(@RequestParam(value = "nameProduct",required = false) String nameProduct,@RequestParam(name="page",defaultValue = "0") int page,Model model) {

        Pageable pageable = PageRequest.of(page,20);
        if (nameProduct.isEmpty()) {
            model.addAttribute("listProduct", productRepository.findByStatus("Đã duyệt",pageable));
        } else {
            model.addAttribute("listProduct", productRepository.findByStatusAndProductNameContains("Đã duyệt",nameProduct,pageable));
        }
        String idCategory = null;
        model.addAttribute("drowdownCategory", categoryRepository.findAll());
        model.addAttribute("category", categoryRepository.findTop());
        model.addAttribute("idCategory", idCategory);
        model.addAttribute("page",page);
        return "/nha/Search";
    }

    @GetMapping("/searchProduct/{idCategory}")
    public String searchAndNameProduct(@PathVariable(name = "idCategory") int id, @RequestParam("nameProduct") String nameProduct, Model model) {
        if (nameProduct.isEmpty()) {
            model.addAttribute("product", colorService.findProduct("Đã duyệt", id));
        } else {
            switch (nameProduct) {
                case "desc":
                    model.addAttribute("product", colorService.findProductOderByDesc("Đã duyệt", id));
                    break;
                case "asc":
                    model.addAttribute("product", colorService.findProductOderByAsc("Đã duyệt", id));
                    break;
                case "new":
                    model.addAttribute("product", colorService.findProductOderByProductDesc("Đã duyệt", id));
                    break;
                default:
                    model.addAttribute("product", colorService.findProduct("Đã duyệt", id, nameProduct));
                    break;
            }
        }
        String idCategory = String.valueOf(id);
        model.addAttribute("nameProduct",nameProduct);
        model.addAttribute("searchProduct", colorService.findProduct("Đã duyệt", id));
        model.addAttribute("drowdownCategory", categoryRepository.findAll());
        model.addAttribute("category", categoryRepository.findTop());
        model.addAttribute("idCategory", idCategory);

        return "/nha/Search";

    }

}
