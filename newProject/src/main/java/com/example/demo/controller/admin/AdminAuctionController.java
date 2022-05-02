package com.example.demo.controller.admin;

import com.example.demo.model.AccUser;
import com.example.demo.model.Auction;
import com.example.demo.repository.UserRepository.UserRepository;
import com.example.demo.repository.auctionRepository.AuctionRepository;
import com.example.demo.service.auctionService.AuctionService;
import com.example.demo.service.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.sql.Date;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminAuctionController {
    @Autowired
    AuctionService auctionService;
    @Autowired
    private AuctionRepository auctionRepository;

    @Autowired
    ProductService productService;

    @Autowired
    UserRepository userRepo;
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
    @GetMapping("/auction/list")
    public String listAuction(@RequestParam(name = "dateStart")String dateStart,
                              @RequestParam(name = "dateEnd")String dateEnd, Model model){
        if(dateStart.isEmpty() && dateEnd.isEmpty()){
            model.addAttribute("auctionlist",auctionRepository.findAll());
        } else if(dateStart.isEmpty()){
            System.out.println(dateEnd);
            model.addAttribute("auctionlist",auctionRepository.findBySearchDayEnd(dateEnd));
        } else if(dateEnd.isEmpty()){
            System.out.println(dateStart);
            model.addAttribute("auctionlist",auctionRepository.findBySearchDayStart(dateStart));
        } else  {
            System.out.println(dateStart +"/"+dateEnd);
            model.addAttribute("auctionlist",auctionRepository.findBySearchDayStartAndDayEnd(dateStart,dateEnd));
        }
        return "/nha/admin/auction/list";
    }

    @GetMapping("/auction/delete/{idAuction}")
    public String deleteAuction(@PathVariable int idAuction, RedirectAttributes redirectAttributesl){
        Auction auction = auctionService.findById(idAuction);
        productService.delete(auction.getProduct().getIdProduct());
        redirectAttributesl.addFlashAttribute("mgseauction", "Deleted!");
        return "redirect:/admin/auction/list";
    }

    @GetMapping("/auction/edit/{idAuction}")
    public String editAuction(@PathVariable int idAuction, Model model){
        model.addAttribute("auction",auctionService.findById(idAuction));
        return "/nha/admin/auction/edit";
    }

    @PostMapping("/auction/edit")
    public String submitEditAuction(Auction auction,RedirectAttributes redirectAttributes){
        auctionService.save(auction);
        redirectAttributes.addFlashAttribute("mgseauction" , "Update " +auction.getProduct().getProductName()+"success");
        return "redirect:/admin/auction/list?dateStart=&dateEnd=";
    }


    

}
