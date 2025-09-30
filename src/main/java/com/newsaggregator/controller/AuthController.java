package com.newsaggregator.controller;

import com.newsaggregator.model.User;
import com.newsaggregator.repository.UserRepository;
import com.newsaggregator.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Username already exists");
        }

       
        user.setPassword(passwordEncoder.encode(user.getPassword()));

       
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("USER");
        }

        userRepository.save(user);
        return ResponseEntity.ok(" User registered successfully");
    }

    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginRequest) {
        Optional<User> optionalUser = userRepository.findByUsername(loginRequest.getUsername());

        if (optionalUser.isPresent() && passwordEncoder.matches(loginRequest.getPassword(), optionalUser.get().getPassword())) {
            String token = jwtUtil.generateToken(loginRequest.getUsername());

            
            return ResponseEntity.ok().body(Map.of("token", token,"username", optionalUser.get().getUsername()));
        }
        return ResponseEntity.status(401).body(Map.of("error", "Invalid username or password"));
    }

}
