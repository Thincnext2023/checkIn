package com.example.Checkin.service;

import com.example.Checkin.entity.CheckInDetails;
import com.example.Checkin.entity.User;
import com.example.Checkin.repository.CheckInDetailsRepo;
import com.example.Checkin.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService{
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    @Autowired
    CheckInDetailsRepo checkInDetailsRepo;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Transactional
    public User registartionUser(String email, String password, String role) {
        if (userRepository.findByEmail(email)!=null) {  // ✅ Fix: Check if user exists
            throw new RuntimeException("User already exists");
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);

        return userRepository.save(user);
    }

    public User loginUser(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("Email doesn't exist");
        }
        if (passwordEncoder.matches(password, user.getPassword())) {
            return user;  // ✅ Reuse the fetched user
        } else {
            throw new RuntimeException("Invalid password");
        }
    }

    @Transactional
    public CheckInDetails checkIn(Long userId,String name,Long phnoTemp, String whomeTo,String purpose){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        CheckInDetails checkIn=new CheckInDetails();
        checkIn.setName(name);
        checkIn.setPhnoTemp(phnoTemp);
        checkIn.setWhomeTo(whomeTo);
        checkIn.setPurpose(purpose);
        checkIn.setUserId(userId);
        checkInDetailsRepo.save(checkIn);
        return checkIn;
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> userOptional = Optional.ofNullable(userRepository.findByEmail(email));
        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
        User user = userOptional.get();
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                new ArrayList<>()  // Empty list for authorities (update if roles are needed)
        );
    }
}
