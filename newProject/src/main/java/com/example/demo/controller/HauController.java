package com.example.demo.controller;

import com.example.demo.config.Utility;
import com.example.demo.model.AccUser;
import com.example.demo.model.Account;
import com.example.demo.model.Bill;
import com.example.demo.repository.UserRepository.UserRepository;
import com.example.demo.service.accountService.AccountService;
import com.example.demo.service.accountService.AccountServiceImpl;
import com.example.demo.service.productBillService.BillService;
import com.example.demo.service.userService.UserService;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.socket.messaging.SessionConnectedEvent;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.security.auth.login.AccountNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.List;

@Controller
public class HauController {
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepo;
    @Autowired
    AccountService accountSerivce;
    @Autowired
    BillService billService;
    @Autowired
    AccountServiceImpl accountSerivceImpl;
    @Autowired
    private JavaMailSender mailSender;

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

    @GetMapping(value = "/ProfileDetail")
    public String showMemberView(Model model, Principal principal) {
//        String idAccount = principal.getName();
        AccUser user = userRepo.findByAccount_IdAccount(principal.getName());
//        AccUser user = userService.findById(idUser);

        model.addAttribute("users", user);
        return "Hau/ProfileDetail";
    }

    @GetMapping(value = "/editMember")
    public String showMemberEdit(Principal principal, Model model) {
        AccUser user = userRepo.findByAccount_IdAccount(principal.getName());
//        AccUser user = userService.findById(idUser);
        model.addAttribute("users", user);
        model.addAttribute("userName", user.getIdUser());
        return "Hau/ProfileDetail";
    }

    @PostMapping("/editMember")
    public String editMember(@Validated @ModelAttribute("users") AccUser users, BindingResult bindingResult, Model model, Principal principal) {
        if (bindingResult.hasFieldErrors()) {
            model.addAttribute("userName", users.getIdUser());
            return "Hau/ProfileDetail";
        } else {
            AccUser userOld = userRepo.findByAccount_IdAccount(principal.getName());
            if (users.equals(userOld)) {
                model.addAttribute("message", "Không có thay đổi nào để cập Nhật !");
                model.addAttribute("userName", users.getIdUser());
                return "Hau/ProfileDetail";
            } else {
                userService.save(users);
                model.addAttribute("message", "Cập Nhật Thành Công !");
                model.addAttribute("userName", users.getIdUser());
                return "Hau/ProfileDetail";
            }
        }
    }

    @GetMapping(value = "/editPass")
    public String showMemberEditPass(Principal principal, Model model) {
        Account account1 = accountSerivce.findById(principal.getName());
//        Account account1 = accountSerivce.findById(account);
        model.addAttribute("account1", account1);
        return "/Hau/Pass";
    }

    @PostMapping("/editPass")
    public String editPass(@RequestParam("psw") String psw, @ModelAttribute("account")
            Account account, Model model, RedirectAttributes redirectAttributes) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String oldPass = passwordEncoder.encode(psw);
        String oldPass1 = accountSerivce.findByPassword(account.getIdAccount());
//        System.out.println("day la pass ms nhap" + psw);
//        System.out.println("day là pass cu " + oldPass1);
//        if (bindingResult.hasFieldErrors()) {
//            model.addAttribute("account1", account.getIdAccount());
//            return "/Hau/Pass";
//        } else {


        if (BCrypt.checkpw(psw, oldPass1)) {
            if (account.getPassword().equals(account.getRePassword())) {
                account.setPassword(passwordEncoder.encode(account.getPassword()));
                account.setRePassword(passwordEncoder.encode(account.getRePassword()));
                accountSerivce.save(account);
//                redirectAttributes.addAttribute("account", account.getIdAccount()).
//                        addFlashAttribute("message", "Cập nhật thành công ! ");
                redirectAttributes.addFlashAttribute("message", "Cập nhật thành công ! ");
                return "redirect:/editPass/";
            } else {
//                    System.out.println("day la mat khau moi " + account.getPassword());
//                    System.out.println("day la mat khau moi xac nhan" + account.getRePassword());
                redirectAttributes.addFlashAttribute("messages", "Mật khẩu phải trùng nhau ! ");
            }
        } else {
            redirectAttributes.addFlashAttribute("messagess", "Mật Khẩu Không Đúng ! ");
        }
        return "redirect:/editPass/";
    }
//    }

    @GetMapping(value = "/listOfInvoices")
    public String showHistory(Model model, Principal principal) {
        AccUser user = userRepo.findByAccount_IdAccount(principal.getName());
//        System.out.println(principal.getName());
        List<Bill> bills = billService.findBills(user.getIdUser());
        model.addAttribute("bills", bills);
        model.addAttribute("users", user);
        return "Hau/UserBill";
    }

    @GetMapping(value = "/revenue")
    public String revenueUser(Model model, Principal principal) {
        AccUser user = userRepo.findByAccount_IdAccount(principal.getName());
        String bills = billService.findRevenue(user.getIdUser());
        model.addAttribute("revenue", bills);
        return "Hau/test";
    }
//    private Integer getUserId(Principal principal) {
//        String userString = principal.toString();
//        int startIndex = userString.indexOf("id=");
//        int endIndex = userString.indexOf(",", startIndex);
//        return Integer.valueOf(userString.substring(startIndex + 3, endIndex));
//    }

    @GetMapping("/forgot_password")
    public String showForgotPasswordForm(Model model) {
//        model.addAttribute("pageTitle", "Forgot Password");
        return "Hau/forgotPassword";

    }

    @PostMapping("/forgot_password")
    public String processForgotPasswordForm(HttpServletRequest request, Model model) {
        String email = request.getParameter("email");
        String token = RandomString.make(45);
        try {
            accountSerivceImpl.updateResetPasswordToken(token, email);
            String resetPasswordLink = Utility.getSiteURL(request) + "/reset_password?token=" + token;
            System.out.println(resetPasswordLink);
            sendMail(email, resetPasswordLink);
            model.addAttribute("message", "We have sent a reset password link to your email. Please check email");
        } catch (AccountNotFoundException e) {
            System.out.println("day la code " + token);
            model.addAttribute("error", "Not Found Email!!");
        } catch (UnsupportedEncodingException | MessagingException e) {
            model.addAttribute("error", "Error while sending email !!");

        }
        return "Hau/forgotPassword";
    }

    private void sendMail(String email, String resetPasswordLink) throws UnsupportedEncodingException, MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom("nhokhau1603@gmail.com", "Neffex Support");
        helper.setTo(email);
        String subject = "Here's the link to your reset password";
        String content = "<p> Hello ,Mr/Mrs</p>"
                + "<p>Your have requested to reset your password.</p>"
                + "<p>Clink the link to change your password</p>"
                + "<p><a href=\"" + resetPasswordLink + "\">Click to change my password</a></p>"
                + "<p>Ignore this email if you do remember your password,or you have not made the request</p>";
        helper.setSubject(subject);
        helper.setText(content, true);
        mailSender.send(message);
    }

    @GetMapping("/reset_password")
    public String showResetPasswordForm(@Param(value = "token") String token, Model model) {
        Account account = accountSerivceImpl.get(token);
        System.out.println("khon tim thay account " + account);
        if (account == null) {
            model.addAttribute("messageError", "Invalid Token");
            return "Hau/ResetPassword";
        }
        model.addAttribute("token", token);
        return "Hau/ResetPassword";
    }

    @PostMapping("/reset_password")
    public String processResetPassword(HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
        String token = request.getParameter("token");
        String password = request.getParameter("password");
        String confirmPass = request.getParameter("confirmPass");
        Account account = accountSerivceImpl.get(token);
//        System.out.println(account);
        if (account == null) {
            model.addAttribute("messageError", "Invalid Token");
            return "Hau/ResetPassword";
        } else {
            if (password.equals(confirmPass)) {
                accountSerivceImpl.updatePassword(account, password);
            } else {
                redirectAttributes.addFlashAttribute("messageError", "Password Incorrect");
                return "redirect:/reset_password?token=" + token;

            }
        }
        redirectAttributes.addFlashAttribute("message", "You have successfully changed your password");
        return "redirect:/login";
    }
}
