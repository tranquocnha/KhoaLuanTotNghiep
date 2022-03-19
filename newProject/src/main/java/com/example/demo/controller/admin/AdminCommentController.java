package com.example.demo.controller.admin;

import com.example.demo.model.AccUser;
import com.example.demo.repository.UserRepository.UserRepository;
import com.example.demo.service.commentService.CommentService;
import com.example.demo.service.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class AdminCommentController {
    @Autowired
    ProductService productService;

    @Autowired
    UserRepository userRepo;
    @Autowired
    CommentService commentService;

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

    @GetMapping("/comment/list")
    public String productCommentList(Model model, @RequestParam(defaultValue = "0") int page){
        Pageable pageableSort = PageRequest.of(page, 5);
        model.addAttribute("product_comment",productService.findProductAndComment(pageableSort));
        return "/nha/comment/list";
    }
    @GetMapping("/comment/list/{idProduct}")
    public String commentList(@PathVariable(value = "idProduct") int idProduct,@RequestParam(defaultValue = "0") int page,Model model){
        Pageable pageableSort = PageRequest.of(page, 5);
        model.addAttribute("listComment",commentService.findByProduct(idProduct,pageableSort));
        model.addAttribute("idProduct",idProduct);
        return "/nha/comment/listComment";
    }
    @GetMapping("/comment/delete/{idComment}")
    public String deleteComment(@PathVariable(value = "idComment")int idCommnet, RedirectAttributes redirectAttributes){
        commentService.delete(idCommnet);
        redirectAttributes.addFlashAttribute("mgsecomment","Xóa Bình Luận Thành Công");
        return "redirect:/comment/list/"+idCommnet;
    }
    @ExceptionHandler(Exception.class)
    public String showErrorPage(Exception e, Model model){
        model.addAttribute("message", e.getMessage());
        return "/Vinh/ErrorPage";
    }
}
