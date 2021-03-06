package com.example.demo.controller;

import com.example.demo.model.AccUser;
import com.example.demo.model.Account;
import com.example.demo.model.Color;
import com.example.demo.model.Product;
import com.example.demo.repository.UserRepository.UserRepository;
import com.example.demo.service.accountService.AccountService;
import com.example.demo.service.categoryService.CategoryService;
import com.example.demo.service.colorService.ColorService;
import com.example.demo.service.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Controller
public class ProductController {
    @Autowired
    UserRepository userRepo;

    @Autowired
    ProductService productService;

    @Autowired
    AccountService accountService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    ColorService colorService;

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
                return "l?? admin";
            }
        } else {
            if (SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().
                    anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_SALER"))) {
                return "l?? saler";
            }
        }
        return null;
    }


    @GetMapping(value = "/product/list")
    public String user(Model model, Principal principal, @PageableDefault(size = 4) Pageable pageable) {
        String userName = principal.getName();
        Page<Color> list=colorService.findAllByProduct(userName,pageable);
        model.addAttribute("listProduct",list );
        return "vuong/ListProductSaler";
    }

    @GetMapping(value = "/product/listApproved")
    public String userNotApprovedYet(Product product, Model model, Principal principal, @PageableDefault(size = 4) Pageable pageable) {
        String userName = principal.getName();
        model.addAttribute("listSP", colorService.findAllApprovedProduct("Ch??a duy???t", userName,pageable));
        return "/vuong/listchuaduyet";
    }

    @GetMapping(value = "/product/upProductMoney")
    public String formUpdateProduct(Product product, Model model, Principal principal, @PageableDefault(size = 4) Pageable pageable) {
        String userName = principal.getName();
        model.addAttribute("listSP", productService.findAllProductByNotApprovedYet(pageable,"No money", userName));
        return "/vuong/listNoUpdateMoney";
    }
    @GetMapping(value = "/product/waitList")
    public String formWaitList(Model model, Principal principal) {
        model.addAttribute("listProductColor", colorService.findByProduct_Status("No money"));
        return "/vuong/waitList";
    }


    @GetMapping(value = "/product/NotApprove")
    public String userNotApprove(Product product, Model model, Principal principal) {
        String userName = principal.getName();
        model.addAttribute("listSP", productService.findAllByNotApprovedYet("Kh??ng duy???t", userName));
        return "/vuong/listkhongduyet";
    }

    @GetMapping(value = "/product/create")
    public String viewCreate(Model model, Principal principal) {
        Product product = new Product();
        LocalDate localDate = LocalDate.now();
        product.setDatePost(localDate.toString());
        model.addAttribute("products", product);
        model.addAttribute("userName", principal.getName());
        model.addAttribute("account", accountService.findAll());
        model.addAttribute("category", categoryService.findAll());
        model.addAttribute("color", colorService.findAll());
        model.addAttribute("notApprovedYet", "No money");
        return "/vuong/create_nguoidung";
    }

    @PostMapping(value = "/product/create")
    public String create(@Valid @ModelAttribute("products") Product product, BindingResult bindingResult, Model model, Principal principal , RedirectAttributes redirectAttributes) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        long millis = System.currentTimeMillis();
        Date date = new Date(millis);
        System.out.println(LocalDateTime.now());
        String dateFormat = format.format(date);
        System.out.println("create product" + product);
        String idAccount = principal.getName();

        product.setAccounts(new Account(idAccount));
        if (bindingResult.hasFieldErrors()) {
            model.addAttribute("userName", principal.getName());
            model.addAttribute("account", accountService.findAll());
            model.addAttribute("category", categoryService.findAll());
            model.addAttribute("color", colorService.findAll());
            model.addAttribute("notApprovedYet", "notUpdate");
            return "/vuong/create_nguoidung";
        }
        this.productService.save(product);
        model.addAttribute("listProduct", colorService.findAllProduct(idAccount));
        redirectAttributes.addFlashAttribute("mgs", "th??m m???i s???n ph???m th??nh c??ng");
//        model.addAttribute("mgs", "th??m m???i s???n ph???m th??nh c??ng");
//        return "/vuong/ListProductSaler";
        return "redirect:/product/list";
    }

    @GetMapping(value = "/product/view")
    public String viewView(@RequestParam("id") Integer id, Model model, Principal principal) {
        if (SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_SALER"))) {
            model.addAttribute("admin", "l?? admin");
        }
        AccUser user = userRepo.findByAccount_IdAccount(principal.getName());
        model.addAttribute("userNames", user);
        model.addAttribute("products", productService.findById(id));
        model.addAttribute("category", categoryService.findAll());
        return "/vuong/view";
    }

    @GetMapping(value = "product/edit")
    public String viewEdit(@RequestParam("id") Integer id, Model model, Principal principal) {
        model.addAttribute("products", productService.findById(id));
        model.addAttribute("category", categoryService.findAll());
        model.addAttribute("notApprovedYet11", "Ch??a duy???t");
        model.addAttribute("waiting", "No money");
        return "/vuong/edit";
    }

    @PostMapping(value = "/product/edit")
    public String edit(@Valid @ModelAttribute("product") Product product, BindingResult bindingResult, Model model, Principal principal, RedirectAttributes redirectAttributes) {
        String idAccount = principal.getName();
        AccUser user = userRepo.findByAccount_IdAccount(principal.getName());
        if (bindingResult.hasFieldErrors()) {
            model.addAttribute("category", categoryService.findAll());
            return "redirect:/product/edit?id=" + product.getIdProduct();
        }
        this.productService.save(product);
        model.addAttribute("product", colorService.findAllProduct(idAccount));
        model.addAttribute("category", categoryService.findAll());
        redirectAttributes.addFlashAttribute("mgsedit", "S???a s???n ph???m th??nh c??ng");
        System.out.println("userName ------ " + product.getAccounts());
        System.out.println("ten -----------" + product.getStatus());
        System.out.println("ten -----------" + product.getProductName());
        return "redirect:/product/list";
    }


    @GetMapping("/product/productDone/{id}")
    public String doneProduct(@PathVariable int id, Model model, Principal principal) {
        Color color = new Color();
        String userName = principal.getName();
        model.addAttribute("colors", color);
        if (SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_SALER"))) {
            model.addAttribute("saler", "l?? saler");
        }
        AccUser user = userRepo.findByAccount_IdAccount(principal.getName());
        model.addAttribute("userNames", user);
        model.addAttribute("userName", principal.getName());
        model.addAttribute("account", accountService.findAll());
        model.addAttribute("product", productService.findById(id));
        model.addAttribute("productNoMoney", productService.findAllByNotApprovedYet("No money", userName));
        model.addAttribute("stocking", "C??n h??ng");
        return "/vuong/create_color";
    }

    @PostMapping("/product/productDone")
    public String createColor(@Valid @ModelAttribute("colors") Color color, BindingResult bindingResult, Model model, Principal principal) {
        String idAccount = principal.getName();
//        new Product().validate(product, bindingResult);
        if (bindingResult.hasFieldErrors()) {
            model.addAttribute("userName", principal.getName());
            model.addAttribute("notApprovedYet", "notUpdate");
            model.addAttribute("stocking", "C??n h??ng");
            return "/vuong/create_color";
        }
        this.colorService.save(color);
        model.addAttribute("listProduct", colorService.findAllProduct(idAccount));
        model.addAttribute("mgs", "Th??m m???i s???n ph???m th??nh c??ng");
        return "redirect:/product/list";
    }

    @GetMapping(value = "/product/delete/{idProduct}")
    public String deleteProduct(@PathVariable Integer idProduct, Model model, Principal principal,RedirectAttributes redirectAttributes) {
        this.colorService.delete(idProduct);
        String idAccount = principal.getName();
        model.addAttribute("listProduct", colorService.findAllProduct(idAccount));
        model.addAttribute("mgsdelete", "X??a s???n ph???m th??nh c??ng!");
        redirectAttributes.addFlashAttribute("mgsdelete","X??a s???n ph???m th??nh c??ng!");
//        return "/vuong/ListProductSaler";
        return "redirect:/product/list";
    }

    @GetMapping("product/searchWaitingApproval")
    public String searchWaitingApproval(@RequestParam("nameProduct") String nameProduct, Model model, Principal principal) {
        String userName = principal.getName();
        List<Product> products = productService.findByNotApprovedYet("Ch??a duy???t", userName, nameProduct);
        if (products.size() == 0) {
            System.out.println("??ang r???ng n?? coi ??o dai bao nhieu nao  ====================" + products.size());
            model.addAttribute("products", products);
            model.addAttribute("mgstk", "Kh??ng t??m th???y s???n ph???m");
            return "/vuong/listchuaduyet";
        } else {
            System.out.println("??ang r???ng n?? coi ??o dai bao nhieu nao  ====================" + products.size());
            model.addAttribute("products", products);
            model.addAttribute("mgstk1", "s???n ph???m ???????c t??m th???y");
            return "/vuong/listchuaduyet";
        }
    }

    @GetMapping("/searchMine")
    public String search(@RequestParam("nameProduct") String nameProduct,
                         Model model, Principal principal) {
        String idAccount = principal.getName();
        List<Product> products = productService.findByNameProduct(idAccount, nameProduct);
        if (products.size() == 0) {
            System.out.println("??ang r???ng n?? coi ??o dai bao nhieu nao  ====================" + products.size());
            model.addAttribute("products", products);
            model.addAttribute("mgstk", "Kh??ng t??m th???y s???n ph???m");
            return "/vuong/ListProductSaler";
        } else {
            model.addAttribute("products", products);
            model.addAttribute("mgstk1", "s???n ph???m ???????c t??m th???y");
            return "/vuong/ListProductSaler";
        }
    }
}
