package com.example.demo.controller;

import com.example.demo.config.MyConstants;
import com.example.demo.model.*;
import com.example.demo.model.DTO.AddressAuctionDTO;
import com.example.demo.model.DTO.AddressDTO;
import com.example.demo.repository.UserRepository.UserRepository;
import com.example.demo.repository.addressRepository.WardRepository;
import com.example.demo.repository.productBillRepository.ProductRepository;
import com.example.demo.repository.tempAuctionRepo.TempAuctionRepository;
import com.example.demo.service.payPalService.PayPalPaymentIntent;
import com.example.demo.service.payPalService.PayPalPaymentMethod;
import com.example.demo.service.payPalService.PayPalService;
import com.example.demo.service.productBillService.BillService;
import com.example.demo.service.userService.UserService;
import com.example.demo.util.PayPalUtils;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@SessionAttributes("tempProduct")
public class PaymentController {
    public static final String URL_PAYPAL_SUCCESS = "pay/successs";
    public static final String URL_PAYPAL_CANCEL = "pay/cancell";
    private Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    private PayPalService paypalService;
    @Autowired
    public JavaMailSender emailSender;
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
    public static Bill billAuction = new Bill();
    public static TempAuction tempAuctionTemp = new TempAuction();
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


    @PostMapping("/payAuction")
    public String payAuction(@Valid @ModelAttribute("addressAuctionDTO") AddressAuctionDTO addressDTO, HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        AccUser user = userService.findByAccount(auth.getName());
        LocalDate currentDate = LocalDate.now();
        Bill bill = new Bill();
        bill.setCurrent(String.valueOf(currentDate));
        bill.setStatus("Chờ lấy hàng");
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
        billAuction = bill;
        tempAuctionTemp = tempAuction;
        String cancelUrl = PayPalUtils.getBaseURL(request) + "/" + URL_PAYPAL_CANCEL;
        String successUrl = PayPalUtils.getBaseURL(request) + "/" + URL_PAYPAL_SUCCESS;
        try {
            Payment payment = paypalService.createPayment(
                    totalMoney,
                    "USD",
                    PayPalPaymentMethod.paypal,
                    PayPalPaymentIntent.sale,
                    "payment description",
                    cancelUrl,
                    successUrl);
            for (Links links : payment.getLinks()) {
                if (links.getRel().equals("approval_url")) {
                    return "redirect:" + links.getHref();
                }
            }
        } catch (PayPalRESTException e) {
            log.error(e.getMessage());
        }
        return "redirect:/";
    }

    @GetMapping(URL_PAYPAL_CANCEL)
    public String cancelPay() {
        return "paypal/cancel";
    }

    @GetMapping(URL_PAYPAL_SUCCESS)
    public String successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String
            payerId, Model model, Principal principal) {
        Double inputTotal = totalMoney;
        List<String> tenSp = new ArrayList<>();
        AccUser user = userRepo.findByAccount_IdAccount(principal.getName());
        MyConstants.setFriendEmail(user.getGmail());
        try {
            Payment payment = paypalService.executePayment(paymentId, payerId);
            if (payment.getState().equals("approved")) {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setFrom("luytlong122@gmail.com");
                message.setTo(MyConstants.FRIEND_EMAIL);
                message.setSubject("THÔNG BÁO ĐÃ THANH TOÁN HÓA ĐƠN!");
                message.setText("Mã hóa đơn: HD" + billAuction.getIdBill() + "\n" +
                        "Danh sách sản phẩm: " + tenSp + "\n" +
                        "Ngày mua: " + billAuction.getCurrent() + "\n" +
                        "Số tiền đã thanh toán: " + totalMoney);
                this.emailSender.send(message);
                model.addAttribute("inputTotal",inputTotal);
                model.addAttribute("product",tempAuctionTemp);
                model.addAttribute("user",user);
                return "/nha/ReceiptPage";
            }
        } catch (PayPalRESTException e) {
            log.error(e.getMessage());
        }
        totalMoney = 0;
        return "redirect:/payPal";
    }

}
