package com.example.demo.controller;

import com.example.demo.model.*;
import com.example.demo.model.DTO.AddressAuctionDTO;
import com.example.demo.model.DTO.AddressDTO;
import com.example.demo.repository.UserRepository.UserRepository;
import com.example.demo.repository.addressRepository.WardRepository;
import com.example.demo.repository.productBillRepository.ProductRepository;
import com.example.demo.repository.tempAuctionRepo.TempAuctionRepository;
import com.example.demo.service.productBillService.BillService;
import com.example.demo.service.userService.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.HashMap;


@Controller
@SessionAttributes("tempProduct")
public class PaymentController {
    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private UserService userService;
    @Autowired
    private WardRepository wardRepository;
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BillService billService;

    @Autowired
    private TempAuctionRepository tempAuctionRepository;

    public static double totalMoney = 0;
    public static int totalQuantity = 0;
    public static ProductBill productDetailBill = new ProductBill();
    public static HashMap<Double, Product> listProductBillTemp = new HashMap<>();
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
    @ModelAttribute("userNames")
    public AccUser getDauGia() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userRepo.findByAccount_IdAccount(auth.getName());
    }
    @GetMapping("/bill/getData")
    public String getHoaDon(@RequestParam String total
            , @RequestParam String quantity
            , Model model
            , @SessionAttribute("carts") HashMap<Integer, Cart> cartMap) {
        System.out.println("cart");
        billGetData(total, quantity, model);

        model.addAttribute("addressDTO", new AddressDTO());
        model.addAttribute("carts",cartMap);
        return "/nha/Pay";
    }

    private void billGetData(@RequestParam String total
            , @RequestParam String quantity
            , Model model) {
        totalMoney = Double.parseDouble(total);
        totalQuantity = Integer.parseInt(quantity);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("total", total);
        model.addAttribute("quantity", quantity);
        model.addAttribute("accUser",userService.findByAccount(auth.getName()));
    }

    @PostMapping("/bill/getData")
    public String getAuction(@RequestParam("idProduct")int idProduct
            , @RequestParam("money") String total
            , @RequestParam("quantity") String quantity
            , Model model)
    {
        System.out.println("auction");
        Product product = productRepository.findById(idProduct).orElse(null);
        TempAuction tempAuction = new TempAuction(product.getAccounts().getIdAccount()
                    ,product.getProductName()
                    , product.getCategory().getCategoryName()
                    , product.getImage1()
                    , product.getImage2()
                    , product.getImage3()
                    , product.getDescription()
                    , product.getDatePost()
            );
            tempAuctionRepository.save(tempAuction);
        product.setStatus("Đã đấu giá");
        productRepository.save(product);
        billGetData(total, quantity, model);
        AddressAuctionDTO addressAuctionDTO = new AddressAuctionDTO();
        addressAuctionDTO.setTotalAuction(Double.parseDouble(total));
        addressAuctionDTO.setIdProduct(tempAuction.getIdProduct());
        model.addAttribute("addressAuctionDTO",addressAuctionDTO);
        model.addAttribute("product",product);
        model.addAttribute("carts",null);
        return "/nha/Pay";
    }

    @PostMapping("/bill/pay/auction")
    public String payAuction(@Valid @ModelAttribute("addressAuctionDTO") AddressAuctionDTO addressDTO, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        AccUser user = userService.findByAccount(auth.getName());
        LocalDate currentDate = LocalDate.now();
        Bill bill = new Bill();
        bill.setCurrent(String.valueOf(currentDate));
        bill.setStatus("Đang giao");
        bill.setUser(user);
        bill.setAddress(addressDTO.getAddress());
        bill.setTotalCost(totalMoney);
        bill.setQuantity(1);
        bill.setWard(wardRepository.findById(Integer.parseInt(addressDTO.getIdWard())).orElse(null));
        billService.save(bill);
        System.out.println(addressDTO.toString());
        TempAuction tempAuction = tempAuctionRepository.findById(addressDTO.getIdProduct()).orElse(null);
        tempAuction.setBliss(bill);
        tempAuctionRepository.save(tempAuction);

        model.addAttribute("inputTotal",addressDTO.getTotalAuction());
        model.addAttribute("product",tempAuction);
        model.addAttribute("user",user);
        return "/nha/ReceiptPage";
    }

    @GetMapping("/bill/history/{idProduct}")
    public String historyBuy(@PathVariable int idProduct,@RequestParam("total") Double total){
        Product product = productRepository.findById(idProduct).orElse(null);
        TempAuction tempAuction = new TempAuction(product.getAccounts().getIdAccount()
                ,product.getProductName()
                , product.getCategory().getCategoryName()
                , product.getImage1()
                , product.getImage2()
                , product.getImage3()
                , product.getDescription()
                , product.getDatePost()
        );
        tempAuctionRepository.save(tempAuction);
        product.setStatus("Đã đấu giá");
        productRepository.save(product);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        AccUser user = userService.findByAccount(auth.getName());
        LocalDate currentDate = LocalDate.now();
        Bill bill = new Bill();
        bill.setCurrent(String.valueOf(currentDate));
        bill.setStatus("Chưa thanh toán");
        bill.setUser(user);
        bill.setTotalCost(total);
        bill.setQuantity(1);
        billService.save(bill);
        tempAuction.setBliss(bill);
        tempAuctionRepository.save(tempAuction);
        return "redirect:/";
    }

}
