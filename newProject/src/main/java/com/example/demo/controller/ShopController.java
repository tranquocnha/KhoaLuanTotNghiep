package com.example.demo.controller;

import com.example.demo.model.AccUser;
import com.example.demo.model.Category;
import com.example.demo.model.Color;
import com.example.demo.model.DiscountCode;
import com.example.demo.service.categoryService.CategoryServiceImpl;
import com.example.demo.service.colorService.ColorServiceImpl;
import com.example.demo.service.discountService.DiscountCodeServiceImpl;
import com.example.demo.service.product.ProductServiceImpl;
import com.example.demo.service.userService.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.List;

@Controller
public class ShopController {
    @Autowired
    ColorServiceImpl colorService;
    @Autowired
    UserServiceImpl userService;
    @Autowired
    ProductServiceImpl productService;
    @Autowired
    CategoryServiceImpl categoryService;
    @Autowired
    DiscountCodeServiceImpl discountCodeService;

    @GetMapping("viewShop/{id}")
    public String showShop(@PathVariable String id, Model model, @PageableDefault(size = 5) Pageable pageable, Principal principal) {
//        String idAccount = principal.getName();
//        String str=id;
        int idUser = Integer.parseInt(id);
        model.addAttribute("idUser", idUser);
        AccUser user = userService.findById(idUser);
        model.addAttribute("users", user);
        int quantity = productService.findByQuantity(idUser);
        model.addAttribute("quantity", quantity);
        List<Category> categoryList = categoryService.findByIdCategory(idUser);
        model.addAttribute("categoryList", categoryList);
        Page<Color> listProduct = colorService.findAllPageById(idUser, pageable);
        model.addAttribute("product", listProduct);
        List<DiscountCode> discountCodeList=discountCodeService.findDiscountCodeByIdSaler(idUser);
        model.addAttribute("discountCodeList", discountCodeList);
        return "Hau/ShopDetail";
    }
    @GetMapping("listDiscount/{id}")
    public String listDiscount(@PathVariable String id,Model model){
        int idUser = Integer.parseInt(id);
        AccUser user = userService.findById(idUser);
        model.addAttribute("users", user);
        List<Category> categoryList = categoryService.findByIdCategory(idUser);
        model.addAttribute("categoryList", categoryList);
        int quantity = productService.findByQuantity(idUser);
        model.addAttribute("quantity", quantity);
        List<DiscountCode> discountCodeList=discountCodeService.findDiscountCodeByIdSaler(idUser);
        model.addAttribute("discountCodeList", discountCodeList);
        return "Hau/listDiscount";
    }
    @GetMapping("shop/{id}")
    public String shop(@PathVariable String id,Model model, @PageableDefault(size = 5) Pageable pageable){
        int idUser = Integer.parseInt(id);
        model.addAttribute("idUser", idUser);
        AccUser user = userService.findById(idUser);
        model.addAttribute("users", user);
        int quantity = productService.findByQuantity(idUser);
        model.addAttribute("quantity", quantity);
        Page<Color> listProduct = colorService.findAllPageById(idUser, pageable);
        model.addAttribute("product", listProduct);
        List<Category> categoryList = categoryService.findByIdCategory(idUser);
        model.addAttribute("categoryList", categoryList);
        return "Hau/ShopNew";
    }
}
