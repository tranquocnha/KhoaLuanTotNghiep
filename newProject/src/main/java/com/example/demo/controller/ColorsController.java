package com.example.demo.controller;

import com.example.demo.model.AccUser;
import com.example.demo.model.Category;
import com.example.demo.model.Color;
import com.example.demo.repository.UserRepository.UserRepository;
import com.example.demo.repository.categoryRepository.CategoryRepository;
import com.example.demo.service.colorService.ColorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class ColorsController {
    @Autowired
    private ColorService colorService;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private CategoryRepository categoryRepository;

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

    @GetMapping("/listcolor")

    public String colorList(Optional<Integer> categoryChoose, Optional<String> colorChoose, @PageableDefault(size = 4) Pageable pageable, Model model) {
        Page<Color> discounts;
        List<Color> colorList = colorService.findListColorByColor();
        model.addAttribute("colorList", colorList);
        List<Category> categoryList = categoryRepository.findAll();
        model.addAttribute("categoryList", categoryList);
        if (categoryChoose.isPresent()) {
            model.addAttribute("categoryChoose", categoryChoose.get());
            discounts = colorService.findColorByCategoryId(categoryChoose.get(), pageable);
            model.addAttribute("colorlist", discounts);
        } else if (colorChoose.isPresent()) {
            model.addAttribute("colorChoose", colorChoose.get());
            discounts = colorService.findColorByColor(colorChoose.get(), pageable);
            model.addAttribute("colorlist", discounts);
//        } else if(categoryChoose.isPresent() && colorChoose.isPresent()) {
        } else if (colorChoose.isPresent() && categoryChoose.isPresent()) {
            model.addAttribute("colorChoose", colorChoose.get());
            model.addAttribute("categoryChoose", categoryChoose.get());
            discounts = colorService.findColorByCategoryIdAndColorName(categoryChoose.get(), colorChoose.get(), pageable);
            model.addAttribute("colorlist", discounts);
        } else {
            discounts = colorService.findAllPage(pageable);
            model.addAttribute("colorlist", discounts);
        }
//    public String colorList(@RequestParam(defaultValue = "0") int page,
//                            Optional<String> nameColor, Optional<String> nameProduct, Model model) {
//        Pageable pageableSort = PageRequest.of(page, 5);
//        if (!nameColor.isPresent()) {
//            if (nameProduct.isPresent()) {
//                model.addAttribute("nameColor", nameColor.get());
////                model.addAttribute("colorlist",null);
//            } else {
//                model.addAttribute("colorlist", colorService.findAllPage(pageableSort));
//            }
//        } else {
//            model.addAttribute("nameColor", nameColor.get());
//            model.addAttribute("nameProduct", nameProduct.get());
//            model.addAttribute("colorlist",null);
//        }
//        model.addAttribute("colorlist", discounts);
        return "/nha/color/list";
    }

    @GetMapping("/ProductByColor")
    public String productByColor(@RequestParam(name = "nameColor") String color, Model model, @PageableDefault(size = 2) Pageable pageable) {
        Page<Color> products = colorService.findColorByColor(color, pageable);
        model.addAttribute("products", products);
        return "Hau/ListColor";

    }

    @GetMapping("/color/delete/{idColor}")
    public String deleteColor(@PathVariable Integer idColor, RedirectAttributes redirectAttributesl) {
        colorService.delete(idColor);
        redirectAttributesl.addFlashAttribute("mgsecolor", "Deleted!!");
        return "redirect:/admin/listcolor";
    }

    @GetMapping("/color/edit/{idColor}")
    public String editColor(@PathVariable int idColor, Model model) {
        model.addAttribute("color", colorService.findById(idColor));
        return "/nha/color/edit";
    }
//    @PostMapping( "/color/edit")
//    public String saveEditColor( Color color,RedirectAttributes redirectAttributes){
//        this.colorService.save(color);
//        redirectAttributes.addFlashAttribute("mgsecolor", "Updated " + color.getColor() +"success!");
//        return "redirect:/admin/listcolor";
//    }
}
