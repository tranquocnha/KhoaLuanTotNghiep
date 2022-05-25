package com.example.demo.controller;

import com.example.demo.model.*;
import com.example.demo.repository.UserRepository.UserRepository;
import com.example.demo.service.accountService.AccountServiceImpl;
import com.example.demo.service.categoryService.CategoryService;
import com.example.demo.service.colorService.ColorServiceImpl;
import com.example.demo.service.commentService.CommentService;
import com.example.demo.service.product.ProductService;
import com.example.demo.service.product.ProductServiceImpl;
import com.example.demo.service.productBillService.BillService;
import com.example.demo.service.productBillService.ProductBillService;
import com.example.demo.service.userService.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/")
@SessionAttributes("carts")
public class BillController {
    @Autowired
    BillService billService;
    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductBillService productBillService;

    @Autowired
    private ColorServiceImpl colorService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private CommentService commentService;
    @Autowired
    private AccountServiceImpl accountService;

    @Autowired
    private ProductServiceImpl productServiceImpl;

    @ModelAttribute("carts")
    public HashMap<Integer, Cart> showInfo() {
        return new HashMap<>();
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

    @GetMapping("")
    public String home(Model model, @SessionAttribute(value = "carts", required = false) HashMap<Integer, Cart> cartMap) {

        List<Category> categoryList;
        categoryList = categoryService.findAll();

        model.addAttribute("category", categoryList);
        if (cartMap != null) {
            model.addAttribute("card", cartMap.size());
        } else {
            model.addAttribute("card", "0");
        }
        model.addAttribute("newProduct", colorService.findProductOderByLIMIT5("Đã duyệt"));
        model.addAttribute("sellerProduct", colorService.findProductOderByDescLIMIT5("Đã duyệt"));
        return "/nha/HomePage";
    }

    @RequestMapping("/a")
    public String index(Model model) {
        List<Category> categoryList;
        categoryList = categoryService.findAll();

        String[] nameCategory = new String[100];
        int i = 0;
        for (Category category : categoryList) {

            String[] s1 = category.getCategoryName().split(" ");
            StringBuilder toString = new StringBuilder();
            for (String s2 : s1) {
                toString.append(s2);
            }
            nameCategory[i] = String.valueOf(toString);
            i++;
            model.addAttribute(String.valueOf(toString), colorService.findProduct("Đã duyệt", category.getIdCategory()));
        }
        model.addAttribute("nameCategory", nameCategory);
        model.addAttribute("category", categoryList);
//        model.addAttribute("MensFashion", colorService.findProduct("Đã duyệt", 1));
//        model.addAttribute("WomanFashion", colorService.findProduct("Đã duyệt", 2));
//        model.addAttribute("Accessory", colorService.findProduct("Đã duyệt", 3));
//        model.addAttribute("Bags", colorService.findProduct("Đã duyệt", 4));
//        model.addAttribute("Camera", colorService.findProduct("Đã duyệt", 5));
//        model.addAttribute("FootwareMan", colorService.findProduct("Đã duyệt", 6));
//        model.addAttribute("FootwareWoman", colorService.findProduct("Đã duyệt", 7));
//        model.addAttribute("Health", colorService.findProduct("Đã duyệt", 8));
//        model.addAttribute("Houseware", colorService.findProduct("Đã duyệt", 9));
//        model.addAttribute("Laptop", colorService.findProduct("Đã duyệt", 10));
//        model.addAttribute("Makeup", colorService.findProduct("Đã duyệt", 11));
//        model.addAttribute("MotherAndBaby", colorService.findProduct("Đã duyệt", 12));
//        model.addAttribute("Smartphone", colorService.findProduct("Đã duyệt", 13));
//        model.addAttribute("Television", colorService.findProduct("Đã duyệt", 14));
//        model.addAttribute("Watch", colorService.findProduct("Đã duyệt", 15));
//        model.addAttribute("Sport", colorService.findProduct("Đã duyệt", 16));
        return "/nha/Home";
    }

    @RequestMapping("/afterLogin")
    public String afterLogin(Model model, Principal principal) {

        List<Category> categoryList;
        categoryList = categoryService.findAll();
        model.addAttribute("category", categoryList);
        for (Category category : categoryList) {
            String[] s1 = category.getCategoryName().split(" ");
            StringBuilder toString = new StringBuilder();
            for (String s2 : s1) {
                toString.append(s2);
            }
            model.addAttribute(String.valueOf(toString), colorService.findProduct("Đã duyệt", category.getIdCategory()));
        }
        return "redirect:/";
    }

    @RequestMapping("/product-detail/{id}")
    public String productDetailBill(@PathVariable int id, Model model) {
        Product product = productService.findByIdStatus("Đã duyệt", id);
        Color color = colorService.findById(id);
        List<Color> colorList = colorService.findByIdProduct(id);
        System.out.println("day la id user");
        String idAccount = productService.findIdAccountByProduct(id);
        model.addAttribute("idAccount", idAccount);
//        int idUser= accountService.findIdUserByIdAccount(idAccount);
//        AccUser accUser=accountService.findAccUserByIdUser(accountService.findIdUserByIdAccount(idAccount));
//        model.addAttribute("accUser", accUser);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getName().equals("anonymousUser")) {
            model.addAttribute("userName", auth.getName());
        }
        model.addAttribute("codeObject", color);
        model.addAttribute("color", colorList);
        model.addAttribute("product", product);
        return "/nha/ProductDetail";
    }

    @RequestMapping("/afterLogin/productDetail/{id}")
    public String afterLoginProductDetailBill(@PathVariable int id) {
        return "redirect:/product-detail/" + id;
    }

    @GetMapping("/search")
    public String search(@RequestParam("idCategory") Integer idCategory,
                         @RequestParam("nameProduct") String nameProduct, Model model) {
        List<Category> categories = categoryService.findAll();
        List<Product> products;
        if (idCategory != 0) {
            if (!nameProduct.equals("")) {
                products = productService.findByCategoryAndNameProduct("Đã duyệt", idCategory, nameProduct);
            } else {
                products = productService.findByCategory("Đã duyệt", idCategory);
            }
        } else {
            if (!nameProduct.equals("")) {
                products = productService.findByNameApproved("Đã duyệt", nameProduct);
            } else {
                products = productService.findByApproved("Đã duyệt");
            }
        }
        if (SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"))) {
            model.addAttribute("admin", "là admin");
        }
        if (categories.size() == 0 || products.size() == 0) {
            model.addAttribute("categories", categories);
            model.addAttribute("products", products);
            model.addAttribute("mgskt", "ko tìm thay");
            return "/nha/Home";
        } else {
            model.addAttribute("categories", categories);
            model.addAttribute("products", products);
            model.addAttribute("mgs", "Danh sách sp tìm thấy");
            return "/nha/Home";
        }
    }

    @GetMapping("detailBill/{idBill}")
    public String detailBill(@PathVariable(name = "idBill") int id
//            ,@PathVariable(name = "idProduct") int idProduct
            ,  Model model, Principal principal) {
        AccUser user = userRepo.findByAccount_IdAccount(principal.getName());
        Bill bills = billService.findBillsById(id);
        System.out.println("day la id bill");
        System.out.println(id);
//        System.out.println(idProduct);
//        Product product=productServiceImpl.findProductByIdProduct();
        model.addAttribute("bills", bills);
        model.addAttribute("users", user);
        return "Hau/BillDetail";
    }

}
