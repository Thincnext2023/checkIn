package com.example.Checkin.controller;

import com.example.Checkin.entity.User;
import com.example.Checkin.repository.CheckInDetailsRepo;
import com.example.Checkin.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/display")
public class DisplayController {
    @Autowired
    private CheckInDetailsRepo checkInDetailsRepo;

    @Autowired
    private UserRepository userRepository;

    public void securityContext(Model model){
        Authentication auth= SecurityContextHolder.getContext().getAuthentication();
        User user=userRepository.findByEmail(auth.getName());
        model.addAttribute("checkInDetails",checkInDetailsRepo.findAll());
        model.addAttribute("userDetails",user);
    }


    @GetMapping("/home")
    public String homePage(Model model){
        securityContext(model);
        return "home";
    }

    @GetMapping("/checkInForm")
    public String checkInForm(Model model){
        securityContext(model);
        return "checkInForm";
    }
    @GetMapping("/main")
    public String main(Model model){
        securityContext(model);
        return "main";
    }

    @GetMapping("/report")
    public String report(Model model){
        securityContext(model);
        return "report";
    }

    @GetMapping("/checkOut")
    public String checkOut(Model model){
        Authentication auth= SecurityContextHolder.getContext().getAuthentication();
        User user=userRepository.findByEmail(auth.getName());
        model.addAttribute("checkInDetails",checkInDetailsRepo.findByStat());
        model.addAttribute("userDetails",user);
        return "checkOut";
    }

    @GetMapping("/reportPassword")
    public String reportPassword(Model model){
        return "reportPassword";
    }

//    @GetMapping("/myProfile")
//    public String myProfile(Model model){
//        Authentication auth=SecurityContextHolder.getContext().getAuthentication();
//        System.out.println(auth);
//        User user=userRepository.findByEmail(auth.getName());
//        model.addAttribute("userDetails",user);
//        return "myProfile";
//    }
//*****Another way by using id from request url******
    @GetMapping("/myProfile/{id}")
    public String myProfile(@PathVariable Long id,Model model){
        Optional<User> user=userRepository.findById(id);
        User userDaya=user.get();
        model.addAttribute("userDetails",userDaya);
        return "myProfile";
    }

    @GetMapping("/register")
    public String register(){
        return "registration.html";
    }


}
