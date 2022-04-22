package com.example.demo.controller;

import com.example.demo.model.AccUser;
import com.example.demo.model.Category;
import com.example.demo.model.DTO.CategoryDTO;
import com.example.demo.repository.UserRepository.UserRepository;
import com.example.demo.service.categoryService.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private UserRepository userRepo;

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
    @GetMapping(value = "/category/list")
    private String viewList(Model model,@RequestParam(name="page",defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page,7);
        model.addAttribute("category", categoryService.findAll(pageable));
        return "/nha/category/list";
    }

    @GetMapping(value = "/category/create")
    private ModelAndView viewCreate() {
        return new ModelAndView("/nha/category/create");
    }

    @PostMapping(value = "/category/createSave")
    private String Create(@RequestParam("categoryName")String categoryName,@RequestParam("categoryImg") MultipartFile categoryImg, Model model, RedirectAttributes redirectAttributes) throws IOException {
        System.out.println("img1"+categoryImg);
        Category category1=new Category();
        category1.setCategoryName(categoryName);
        if(categoryImg == null || categoryImg.isEmpty()){
            redirectAttributes.addFlashAttribute("messFileImg","Error Null and Empty");
            redirectAttributes.addFlashAttribute("saveCategoryName",categoryName);
            redirectAttributes.addFlashAttribute("saveCategoryImg",categoryImg);
            return "redirect:/admin/category/create";
        }
//        if (bindingResult.hasFieldErrors()) {
//            return "/nha/category/create";
//        }
        System.out.println("img2"+categoryImg);
        List<Category> categoryList = categoryService.findByName(categoryName);

        if ( categoryList.size() != 0) {
            model.addAttribute("mgsdm", "Danh mục đã tồn tại.");
            return "/nha/category/create";
        }
        System.out.println("img3"+categoryImg);
        String fileName = categoryImg.getOriginalFilename();
        System.out.println("img4"+fileName);
        try {
            FileCopyUtils.copy(categoryImg.getBytes(), new File("D:\\Java-API\\KhoaLuanTotNghiep\\KhoaLuanTotNghiep\\newProject\\src\\main\\resources\\static\\Images\\" + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        category1.setCategoryImg(fileName);
        categoryService.save(category1);
        redirectAttributes.addFlashAttribute("mgsedit", "Thêm mới danh mục thành công.");
        return "redirect:/admin/category/list";
    }

    @GetMapping(value = "/category/edit")
    public String ViewEdit(@RequestParam("id") Integer id, Model model) {
        model.addAttribute("category", categoryService.findById(id));
        return "/nha/category/edit";
    }

    @PostMapping(value = "/category/edit")
    public String Edit(Category category,RedirectAttributes redirectAttributes) {
        this.categoryService.save(category);
        redirectAttributes.addFlashAttribute("mgsedit", "Sửa danh mục thành công.");

        return "redirect:/admin/category/list";
    }

    @GetMapping(value = "/category/delete/{idCategory}")
    public String delete(@PathVariable int idCategory,RedirectAttributes redirectAttributes) {
    
        this.categoryService.delete(idCategory);
        redirectAttributes.addFlashAttribute("mgsedit", "Xóa danh mục thành công.");
        return "redirect:/admin/category/list";
    }
}
