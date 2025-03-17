package com.example.Checkin.controller;

import com.example.Checkin.repository.CheckInDetailsRepo;
import com.example.Checkin.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class LoginController {

    @Autowired
    private CheckInDetailsRepo checkInDetailsRepo;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/login")
    public String loginPage(Model model) {
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal())) {
            model.addAttribute("checkInDetails",checkInDetailsRepo.findAll());
            return "main";  // Redirect if the user is authenticated
        }   
        return "login";  // Show login page if the user is not authenticated
    }

}
