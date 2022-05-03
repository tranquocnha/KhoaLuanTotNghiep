package com.example.demo.controller.admin;

import com.example.demo.model.AccUser;
import com.example.demo.model.Color;
import com.example.demo.model.Product;
import com.example.demo.repository.UserRepository.UserRepository;
import com.example.demo.service.categoryService.CategoryService;
import com.example.demo.service.colorService.ColorService;
import com.example.demo.service.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    UserRepository userRepo;
    @Autowired
    ProductService productService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    ColorService colorService;

    @GetMapping(value="")
    public String AdminHome(Model model, Principal principal) {
        // xet duyet account
        AccUser user = userRepo.findByAccount_IdAccount(principal.getName());
        model.addAttribute("userNames", user);
        return "/nha/admin/HomeAdmin";
    }
    @ModelAttribute("admin")
    public String AdminOrSaler(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
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
    @GetMapping(value = "/list")
    public String AdminList(@RequestParam(value = "page", defaultValue = "1") int page, Model model, Principal principal) {
        AccUser user = userRepo.findByAccount_IdAccount(principal.getName());
        model.addAttribute("userNames", user);
        //tim kiem phan trang theo ten san pham
        Sort sort = Sort.by("productName").descending();
        model.addAttribute("product", productService.findAllProduct(PageRequest.of(page, 10, sort)));
        return "/nha/admin/ListProduct";
    }

    @GetMapping(value = "/view")
    public String AdminView(@RequestParam("id") int id, Model model, Principal principal) {
        AccUser user = userRepo.findByAccount_IdAccount(principal.getName());
        model.addAttribute("userNames", user);
        model.addAttribute("product", productService.findById(id));
        model.addAttribute("category", categoryService.findAll());
        return "/nha/admin/ViewProduct";
    }

    @GetMapping(value = "/{idProduct}/deleteProduct")
    public String delete(@PathVariable int idProduct , @ModelAttribute("product") Product product, Model model, Principal principal) {
        AccUser user = userRepo.findByAccount_IdAccount(principal.getName());
        model.addAttribute("userNames", user);
        System.out.println("ID Product is :----------------------------" + idProduct);
        productService.delete(idProduct);
        return "redirect:/admin/list";
    }

    @GetMapping(value = "/approve")
    public String AdminApprove(Model model, Principal principal) {
        AccUser user = userRepo.findByAccount_IdAccount(principal.getName());
        model.addAttribute("userNames", user);
        model.addAttribute("product", productService.findByStatus("Chưa duyệt"));
        return "nha/admin/DuyetProduct";
    }
    @GetMapping(value = "/approve-color/{idProduct}")
    public String AdminColorApprove(@PathVariable(value = "idProduct") int idProduct,Model model, Principal principal) {
        AccUser user = userRepo.findByAccount_IdAccount(principal.getName());
        model.addAttribute("userNames", user);
        model.addAttribute("listColor",colorService.findByIdProduct(idProduct));
        return "nha/admin/DuyetProductListColor";
    }
    @PostMapping(value = "/approve-color/delete/{idColor}/{idProduct}")
    public String AdminColorApproveDelete(@PathVariable(value = "idColor") int idColor,
                                          @PathVariable(value = "idProduct") int idProduct,
                                          RedirectAttributes redirectAttributes){
        colorService.delete(idColor);
        redirectAttributes.addFlashAttribute("mgsecolor","Xóa Màu Thành Công Mã Màu là: " +idColor);
        return "redirect:/approve-color/"+idProduct;
    }
    @PostMapping(value = "/approved")
    public String AdminCreate(@RequestParam("submit") String submit, Product product, Model model, RedirectAttributes redirectAttributes, Principal principal) {
        if (submit.equals("duyet")) {
            AccUser user = userRepo.findByAccount_IdAccount(principal.getName());
            model.addAttribute("userNames", user);
            product.setStatus("Đã duyệt");
            this.productService.save(product);
            redirectAttributes.addFlashAttribute("mgs1", "Phê duyệt sản phẩm thành công!");
            return "redirect:/admin/approve";
        } else {
            AccUser user = userRepo.findByAccount_IdAccount(principal.getName());
            model.addAttribute("userNames", user);
            product.setStatus("Không duyệt");
            this.productService.save(product);
            return "redirect:/admin/approve";
        }
    }

    @GetMapping(value = "/deleteNotApprovedYet/{idProduct}")
    public String deleteNotApprovedYet(@PathVariable int idProduct, Model model, Principal principal) {
        AccUser user = userRepo.findByAccount_IdAccount(principal.getName());
        model.addAttribute("userNames", user);
        this.productService.delete(idProduct);
        return "redirect:/admin/approve";
    }

    @GetMapping(value = "/edit")
    public String AdminViewEdit(@RequestParam("id") Integer id, Model model, Principal principal) {
        AccUser user = userRepo.findByAccount_IdAccount(principal.getName());
        model.addAttribute("userNames", user);
        model.addAttribute("product", productService.findById(id));
        model.addAttribute("category", categoryService.findAll());
        model.addAttribute("notApprovedYet", "Chưa duyệt");
        model.addAttribute("approved", "Đã duyệt");
        model.addAttribute("noApproved", "Không duyệt");
        return "/nha/admin/EditProduct";
    }

    @PostMapping(value = "/edit")
    public String AdminEdit(@ModelAttribute("product") Product product, Model model, RedirectAttributes redirectAttributes, Principal principal) {
        AccUser user = userRepo.findByAccount_IdAccount(principal.getName());
        model.addAttribute("userNames", user);
        this.productService.save(product);
        redirectAttributes.addFlashAttribute("mgs2", "sửa sản phẩm thành công!");
        return "redirect:/admin/list";
    }

    @GetMapping(value = "/search")
    public String search(@RequestParam("nameProduct") String nameProduct, Model model, Principal principal) {
        List<Product> products = productService.findByName(nameProduct);
        if (products.size() == 0) {
            model.addAttribute("product", products);
            model.addAttribute("mgs", "khomg tim thay sp");
            return "/nha/admin/ListProduct";
        } else {
            AccUser user = userRepo.findByAccount_IdAccount(principal.getName());
            model.addAttribute("userNames", user);
            Sort sort = Sort.by("nameProduct").descending();
//            model.addAttribute("sanphams1", sanPhamService.findByNameadmin(tenSanPham);
            model.addAttribute("product", products);
            return "/nha/admin/ListProduct";
        }
    }

    @GetMapping(value = "/searchApproved")
    public String searchApproved(@RequestParam("nameProduct") String nameProduct, Model model) {
        List<Product> products = productService.findByNameApproved("Chưa duyệt", nameProduct);
        if (products.size() == 0) {
            model.addAttribute("products", products);
            model.addAttribute("mgs", "khomg tim thay sp");
            return "/nha/admin/DuyetProduct";
        } else {
            model.addAttribute("products", products);
            return "/nha/admin/DuyetProduct";
        }
    }
}
