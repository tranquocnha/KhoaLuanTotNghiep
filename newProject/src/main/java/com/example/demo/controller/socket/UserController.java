package com.example.demo.controller.socket;

import com.example.demo.model.AccUser;
import com.example.demo.model.ActivityLogEntity;
import com.example.demo.repository.UserRepository.UserRepository;
import com.example.demo.repository.socket.ActivityLogEntityRepo;
import com.example.demo.service.socket.AccUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Set;

@Controller
@CrossOrigin
public class UserController {
    @Autowired
    private AccUserService userServiceInterface;
    @Autowired
    private ActivityLogEntityRepo activityLogEntityRepo;
    @Autowired
    private UserRepository userRepo;
    @ModelAttribute("userNames")
    public AccUser getDauGia() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userRepo.findByAccount_IdAccount(auth.getName());
    }
    @GetMapping(value="/chats", consumes= MediaType.ALL_VALUE)
    public String chat(Model model, Principal principal) {
        model.addAttribute("account",principal.getName());
        return "nha/socket/chatMessager";
    }

    @GetMapping("/fetchAllUsers")
    @ResponseBody
    public List<AccUser> fetchAll() {
        return userServiceInterface.findAllByName();
    }

    @GetMapping("/registration/{userName}")
    public ResponseEntity<Void> register(@PathVariable String userName) {
        System.out.println("handling register user request: " + userName);
        AccUser user = userServiceInterface.findByUserName(userName);
        ActivityLogEntity entity = new ActivityLogEntity();
        entity.setUserName(user.getName());
        entity.setActivity(ActivityLogEntity.ActivityType.LOGIN);
        activityLogEntityRepo.save(entity);
        return ResponseEntity.ok().build();
    }

}
