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
                return "l?? admin";
            }
        }else{
            if (SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().
                    anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_SALER"))) {
                return "l?? saler";
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

        model.addAttribute("MensFashion", productRepository.findByAuction("???? duy???t", 1));
        model.addAttribute("WomanFashion", productRepository.findByAuction("???? duy???t", 2));
        model.addAttribute("Accessory", productRepository.findByAuction("???? duy???t", 3));
        model.addAttribute("Bags", productRepository.findByAuction("???? duy???t", 4));
        model.addAttribute("Camera", productRepository.findByAuction("???? duy???t", 5));
        model.addAttribute("FootwareMan", productRepository.findByAuction("???? duy???t", 6));
        model.addAttribute("FootwareWoman", productRepository.findByAuction("???? duy???t", 7));
        model.addAttribute("Health", productRepository.findByAuction("???? duy???t", 8));
        model.addAttribute("Houseware", productRepository.findByAuction("???? duy???t", 9));
        model.addAttribute("Laptop", productRepository.findByAuction("???? duy???t", 10));
        model.addAttribute("Makeup", productRepository.findByAuction("???? duy???t", 11));
        model.addAttribute("MotherAndBaby", productRepository.findByAuction("???? duy???t", 12));
        model.addAttribute("Smartphone", productRepository.findByAuction("???? duy???t", 13));
        model.addAttribute("Television", productRepository.findByAuction("???? duy???t", 14));
        model.addAttribute("Watch", productRepository.findByAuction("???? duy???t", 15));
        model.addAttribute("Sport", productRepository.findByAuction("???? duy???t", 16));
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
        Auction auction = auctionService.findByProduct("???? duy???t",id);
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
        model.addAttribute("sanPham", productService.findByIdStatus("???? duy???t",id));
        model.addAttribute("giaCaoNhat", giaCaoNhat);
        model.addAttribute("giaDau", giaDau);
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
        Auction auction = auctionService.findByProduct("???? duy???t",idSP);
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
                products = productService.findByCategoryAndNameProduct("???? duy???t", maDanhMuc, tenSp);
            } else {
                products = productService.findByCategory("???? duy???t", maDanhMuc);
            }
        } else {
            if (!tenSp.equals("")) {
                products = productService.findByNameApproved("???? duy???t", tenSp);
            } else {
                products = productService.findByApproved("???? duy???t");
            }
        }
        if (SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"))) {
            model.addAttribute("admin", "l?? admin");
        }
        if (categories.size() == 0 || products.size() == 0) {
            model.addAttribute("danhmucs", categories);
            model.addAttribute("listSP", products);
            model.addAttribute("mgskt", "ko t??m thay");
            return "/nha/auction/Home";
        } else {
            model.addAttribute("danhmucs", categories);
            model.addAttribute("listSP", products);
            model.addAttribute("mgs", "Danh s??ch sp t??m th???y");
            return "/nha/auction/Home";
        }
    }

}
