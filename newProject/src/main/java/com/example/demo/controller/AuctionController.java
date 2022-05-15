package com.example.demo.controller;


import com.example.demo.model.*;
import com.example.demo.repository.UserRepository.UserRepository;
import com.example.demo.repository.auctionRepository.AuctionRepository;
import com.example.demo.repository.colorRepository.ColorRepository;
import com.example.demo.repository.productBillRepository.ProductRepository;
import com.example.demo.service.auctionService.AuctionService;
import com.example.demo.service.auctionUserService.AuctionUserService;
import com.example.demo.service.categoryService.CategoryService;
import com.example.demo.service.commentService.CommentService;
import com.example.demo.service.product.ProductService;
import com.example.demo.service.productBillService.ProductBillService;
import com.example.demo.service.userService.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/")
@SessionAttributes("carts")
public class AuctionController {
    private final ProductService productService;
    private final CategoryService categoryService;
    private final  ProductBillService productBillService;
    private final UserService userService;
    private final UserRepository userRepo;
    private final CommentService commentService;
    private final AuctionService auctionService;
    private final ColorRepository colorRepository;
    private final AuctionUserService auctionUserService;
    private final ProductRepository productRepository;

    @Autowired
    public AuctionController(ProductService productService, CategoryService categoryService, ProductBillService productBillService, UserService userService, UserRepository userRepo, CommentService commentService, AuctionService auctionService, ColorRepository colorRepository, AuctionUserService auctionUserService, ProductRepository productRepository) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.productBillService = productBillService;
        this.userService = userService;
        this.userRepo = userRepo;
        this.commentService = commentService;
        this.auctionService = auctionService;
        this.colorRepository = colorRepository;
        this.auctionUserService = auctionUserService;
        this.productRepository = productRepository;
    }
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
    @RequestMapping("/auction")
    public String index(Model model,@SessionAttribute("carts") HashMap<Integer, Cart> cartMap) throws ParseException {
        List<Category> categoryList;
        categoryList = categoryService.findAll();
        model.addAttribute("category", categoryList);
//        LocalDateTime localDateTime = LocalDateTime.now();
//        Date sellDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(String.valueOf(LocalDateTime.now()));
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String dateTime = dtf.format(now);
        Date today = Calendar.getInstance().getTime();
        System.out.println("day today :"+today);
        for (Category category: categoryList) {
            String[] s1=category.getCategoryName().split(" ");
            StringBuilder toString= new StringBuilder();
            for(String s2:s1){
                toString.append(s2);
            }
            model.addAttribute(String.valueOf(toString), productRepository.findByAuction("Đã duyệt", category.getIdCategory()));
        }
        if(cartMap != null) {
            model.addAttribute("card", cartMap.size());
        }else{
            model.addAttribute("card", "0");
        }
        return "/nha/auction/Home";
    }
    @RequestMapping("/afterLogin/auction/")
    public String afterLogin(Model model) {
        return "redirect:/auction";
    }
    @RequestMapping("/auction-detail/{id}")
    public String producDetail(@PathVariable int id, Model model, @SessionAttribute(value = "carts",required = false) HashMap<Integer, Cart> cartMap) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Auction auction = auctionService.findByProduct("Đã duyệt",id);
        List<AuctionUser> detailList = auctionUserService.findByProduct(id);
        double giaCaoNhat = 0;
        if (!detailList.isEmpty()) {
            giaCaoNhat = detailList.get(0).getStartingPrice();
            model.addAttribute("nguoiCaoNhat", detailList.get(0));
        }
        //kiem tra xem neu chua dang nhap thi doi thanh button dang nhap
        if (auth.getName().equals("anonymousUser")) {
            model.addAttribute("userName", auth.getName());
        } else {
            for (AuctionUser auctionUser : detailList) {
                if (auctionUser.getUsers().getAccount().getIdAccount().equals(auth.getName())) {
                    if (auctionUser.getStartingPrice() == giaCaoNhat) {
                        model.addAttribute("winner", userService.findByAccount(auth.getName()));
                    }
                }
            }
        }
        //gia dau cao nhat cong voi gia khoi diem
        double giaDau = giaCaoNhat + 5;
        if(cartMap != null){
            model.addAttribute("cartMap", cartMap);
        }
        model.addAttribute("sanPham", productService.findByIdStatus("Đã duyệt",id));
        model.addAttribute("giaCaoNhat", giaCaoNhat);
        model.addAttribute("giaDau", giaDau);
        model.addAttribute("dauGia", detailList);
        model.addAttribute("producTimeEnd",auction.getAuctionTime());
        return "nha/auction/ProductDetail";
    }
    @RequestMapping("/afterLogin/auction-detail/{id}")
    public String afterLoginproducDetail(@PathVariable int id) {
        return "redirect:/auction-detail/"+id;
    }
    @PostMapping("/dauGia")
    public String dauGia(@RequestParam int idSP, double money, Principal principal) {
        AccUser user = userRepo.findByAccount_IdAccount(principal.getName());
        Auction auction = auctionService.findByProduct("Đã duyệt",idSP);
        if (auction == null) {
            auction = new Auction();
            auction.setProduct(productService.findById(idSP));
            auctionService.save(auction);
        }
        //lay thoi gian hien tai
        Time time = new Time(System.currentTimeMillis());
        //them 2 thuoc tinh khoa
        AuctionUser auctionUser = new AuctionUser();
        auctionUser.setAuctions(auction);
        auctionUser.setUsers(user);
        auctionUser.setAuctionEndTime(time);
        auctionUser.setStartingPrice(money);
        auctionUserService.create(auctionUser);
        return "redirect:/auction-detail/" + idSP;
    }
    @GetMapping("/timKiem")
    public String search(@RequestParam("maDanhMuc") Integer maDanhMuc,
                         @RequestParam("tenSp") String tenSp, Model model) {
        List<Category> categories = categoryService.findAll();
        List<Product> products;
        if (maDanhMuc != 0) {
            if (!tenSp.equals("")) {
                products = productService.findByCategoryAndNameProduct("Đã duyệt", maDanhMuc, tenSp);
            } else {
                products = productService.findByCategory("Đã duyệt", maDanhMuc);
            }
        } else {
            if (!tenSp.equals("")) {
                products = productService.findByNameApproved("Đã duyệt", tenSp);
            } else {
                products = productService.findByApproved("Đã duyệt");
            }
        }
        if (SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"))) {
            model.addAttribute("admin", "là admin");
        }
        if (categories.size() == 0 || products.size() == 0) {
            model.addAttribute("danhmucs", categories);
            model.addAttribute("listSP", products);
            model.addAttribute("mgskt", "ko tìm thay");
            return "/nha/auction/Home";
        } else {
            model.addAttribute("danhmucs", categories);
            model.addAttribute("listSP", products);
            model.addAttribute("mgs", "Danh sách sp tìm thấy");
            return "/nha/auction/Home";
        }
    }

}
