package com.example.demo.controller;


import com.example.demo.config.Utility;
import com.example.demo.model.*;
import com.example.demo.repository.UserRepository.UserRepository;
import com.example.demo.service.accountService.AccountService;
import com.example.demo.service.addressService.AddressService;
import com.example.demo.service.roleService.RoleService;
import com.example.demo.service.userService.UserService;
import com.example.demo.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("")
public class MainController {
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepo;

    @Autowired
    AccountService accountService;

    @Autowired
    RoleService roleService;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    AddressService addressService;
    AccountUser accountUser1;
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
    @RequestMapping(value = "/userInfo", method = RequestMethod.GET)
    public String userInfo(Model model, Principal principal) {

        // Sau khi user login thanh cong se co principal
        String userName = principal.getName();

        System.out.println("User Name: " + userName);

        User loginedUser = (User) ((Authentication) principal).getPrincipal();

        // 1 cái util( dùng chung) dùng để hiển thị principal
        String userInfo = WebUtils.toString(loginedUser);
        model.addAttribute("userInfo", userInfo);

        return "/nha/userInfoPage";
    }

    @GetMapping(value = "/register")
    public String viewSignUp(Model model) {
        model.addAttribute("register", new AccountUser());
        return "/nha/register";
    }

    @PostMapping(value = "/register")
    public String singUp(@Valid @ModelAttribute("register") AccountUser accountUser, BindingResult bindingResult, Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) throws MessagingException, UnsupportedEncodingException {
        new AccountUser().validate(accountUser, bindingResult);
        if (bindingResult.hasErrors()) {
            return "/nha/register";
        }
        Account account = accountService.findById(accountUser.getUserName1());
        List<AccUser> email = userService.findByEmail(accountUser.getGmail1());
        if (account != null && email.size() != 0) {
            model.addAttribute("errTK", "Ten tai khoan da ton tai");
            model.addAttribute("errEmail", "Email da ton tai");
            return "/nha/register";
        }
        if (account != null) {
            model.addAttribute("errTK", "Ten tai khoan da ton tai");
            return "/nha/register";
        }
        if (email.size() != 0) {
            model.addAttribute("errEmail", "Email da ton tai");
            return "/nha/register";
        }



        String userName = accountUser.getUserName1();
        String name = accountUser.getName1();
        String sex = accountUser.getSex1();
        String date = accountUser.getDateTime1();
        String email1 = accountUser.getGmail1();
        int numberCard = accountUser.getNumberCard1();
        String password = accountUser.getPassWord1();
        String rePassword = accountUser.getRePassWord1();
        String phoneUser = accountUser.getPhoneUser1();
        String add = accountUser.getAddress1();


        String registerLink = Utility.getSiteURL(request) + "/confirmRegister?&userName=" + userName
                + "&name=" + name
                + "&sex=" + sex
                + "&date=" + date
                + "&email1=" + email1
                + "&numberCard=" + numberCard
                + "&password=" + password
                + "&phoneUser=" + phoneUser
                + "&address=" + add
                + "&rePassword="+rePassword;

        redirectAttributes.addAttribute("email1", email1);
        redirectAttributes.addAttribute("password", password);
        redirectAttributes.addAttribute("rePassword", rePassword);
        redirectAttributes.addAttribute("phoneUser", phoneUser);
        redirectAttributes.addAttribute("userName", userName);
        redirectAttributes.addAttribute("sex", sex);
        redirectAttributes.addAttribute("numberCard", numberCard);
        redirectAttributes.addAttribute("add", add);
        redirectAttributes.addAttribute("date", date);
        redirectAttributes.addAttribute("name", name);
//
//        address.setAccUser(user);
//        address.setNameAddress(accountUser.getAddress1());
//        user.getAddress().add(address);
//        System.out.println("dia chi " + address);
        System.out.println("repasss " + rePassword);
        sendMailRegister(email1, registerLink);
        return "/nha/login";


//        System.out.println("nguoi dun  ==========" + user);
//        return "redirect:/login";
//        return "Hau/RegisterCheck";
    }

    @GetMapping(value = "/confirmRegister")
    public String page2(
            @RequestParam("userName") String userName,
            @RequestParam("name") String name,
            @RequestParam("address")
                    String add,
            @RequestParam("password")
                    String password,
            @RequestParam("phoneUser")
                    String phoneUser,
            @RequestParam("email1")
                    String email1,
            @RequestParam("sex")
                    String sex,
            @RequestParam("numberCard")
                    int numberCard,
            @RequestParam("date")
                    String date,
            @RequestParam("rePassword")
                    String rePassword,
            AccountUser accountUser,
            Model model) {
        model.addAttribute("email1", email1);
        model.addAttribute("password", password);
        model.addAttribute("phoneUser", phoneUser);
        model.addAttribute("userName", userName);
        model.addAttribute("sex", sex);
        model.addAttribute("numberCard", numberCard);
        model.addAttribute("add", add);
        model.addAttribute("date", date);
        model.addAttribute("rePassword", rePassword);
        model.addAttribute("name", name);
        accountUser1 = new AccountUser(name,sex,date,email1,numberCard,add,phoneUser,userName,password,rePassword);
//        model.addAttribute("roles", roles);

        return "Hau/RegisterCheck";
    }
    @PostMapping("/confirm")
    private String page3(Model model,RedirectAttributes redirectAttributes,Account account) {
        Address address = new Address();
        Set<Role> roles = roleService.findByRoleName("ROLE_CUSTOMER");
        Account account1 = new Account(accountUser1.getUserName1(),bCryptPasswordEncoder.encode(accountUser1.getPassWord1()),accountUser1.getRePassWord1(),null, true, roles);
        AccUser user1 = new AccUser( accountUser1.getName1(), Boolean.parseBoolean(accountUser1.getSex1()),
                accountUser1.getDateTime1(), accountUser1.getGmail1(),
                accountUser1.getNumberCard1(), accountUser1.getPhoneUser1(), account1);

        address.setAccUser(user1);
        address.setNameAddress(accountUser1.getAddress1());
        addressService.save(address);
        redirectAttributes.addFlashAttribute("message", "Successful Account");
        return "redirect:/login";
    }

    private void sendMailRegister(String email, String registerLink) throws UnsupportedEncodingException, MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom("nhokhau1603@gmail.com", "Neffex Support");
        helper.setTo(email);
        String subject = "Here's the link to your register account";
        String content = "<p> Hello ,Mr/Mrs</p>"
                + "<p>Do you want to sign up for a neffex account?.</p>"
                + "<p>Clink the link to register account</p>"
                + "<p><a href=\"" + registerLink + "\">Register account</a></p>"
                + "<p>Ignore this email if you do remember your password,or you have not made the request</p>";
        helper.setSubject(subject);
        helper.setText(content, true);
        mailSender.send(message);
    }


    @GetMapping("/403")
    private String accessDenied(Model model, Principal principal) {
        AccUser user = userService.findByAccount(principal.getName());
        model.addAttribute("user", user);
        model.addAttribute("mgs", "Bạn không có quyền truy cập !");
        return "/Vinh/ErrorPage";
    }
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginPage() {
        return "/nha/login";
    }
}
