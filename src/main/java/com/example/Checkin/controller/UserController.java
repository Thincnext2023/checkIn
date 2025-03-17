package com.example.Checkin.controller;

import com.example.Checkin.entity.CheckInDetails;
import com.example.Checkin.entity.User;
import com.example.Checkin.repository.CheckInDetailsRepo;
import com.example.Checkin.repository.UserRepository;
import com.example.Checkin.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Optional;

@CrossOrigin(origins = "*")
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CheckInDetailsRepo checkInDetailsRepo;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public String registration(@RequestParam String email, @RequestParam String password, @RequestParam String role) {
        userService.registartionUser(email, password, role);
        return "registration";
    }

    @PostMapping("report")
    public String report(@RequestParam String password){
        Authentication auth=SecurityContextHolder.getContext().getAuthentication();
        User user=userRepository.findByEmail(auth.getName());
        PasswordEncoder passwordEncoder=new BCryptPasswordEncoder();
        Boolean value=passwordEncoder.matches(password,user.getPassword());
        if(value){
            return "redirect:/display/report";
        }else {
            return "report";
        }
    }
    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password, RedirectAttributes redirectAttributes, HttpServletRequest request, Model model) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            HttpSession session = request.getSession(true);
            session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
            model.addAttribute("userDetails",userRepository.findByEmail(authentication.getName()));
            model.addAttribute("checkInDetails",checkInDetailsRepo.findAll());
            return "redirect:/display/main"; // Redirect to the desired page after successful login
        } catch (Exception e) {
            System.out.println("Invalid email and password");
            redirectAttributes.addFlashAttribute("error", "Invalid email and password");
            return "login"; // Return to login page on failure
        }
    }

    @PostMapping("/checkIn")
    public String checkInDetails(@RequestParam Long userId,@RequestParam String name, @RequestParam Long phnoTemp, @RequestParam String whomTo, @RequestParam String purpose,Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null) {
            userService.checkIn(userId,name, phnoTemp, whomTo, purpose);
            User user=userRepository.findByEmail(auth.getName());
            model.addAttribute("userDetails",user);
            return "redirect:/display/checkInForm";
        } else {
            System.out.println("Not authenticated");
            return "login";
        }
    }

    @PutMapping("/checkOut/{id}")
    public String checkOut(@PathVariable Long id){
        Optional<CheckInDetails> checkInDetailsData=checkInDetailsRepo.findById(id);
        CheckInDetails checkInDetails=checkInDetailsData.get();
        checkInDetails.setStat("checkOut");
        checkInDetailsRepo.save(checkInDetails);
        return "redirect:/display/checkOut";
    }


}
