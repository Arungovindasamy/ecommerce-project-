package com.example.project001a.service;

import com.example.project001a.dto.AuthRequest;
import com.example.project001a.dto.AuthResponse;
import com.example.project001a.entity.User;
import com.example.project001a.repository.UserRepository;
import com.example.project001a.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @RequiredArgsConstructor
@Transactional
public class AuthService {
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthResponse register(AuthRequest request) {
        User user = userRepo.findByEmail(request.getEmail())
                .orElseGet(() -> {
                    User newUser = User.builder()
                            .name(request.getEmail().split("@")[0])
                            .email(request.getEmail())
                            .password(passwordEncoder.encode(request.getPassword()))
                            .build();
                    return userRepo.save(newUser);
                });
        return new AuthResponse(jwtUtil.generateToken(user.getEmail()));
    }

    public AuthResponse login(AuthRequest request) {
        User user = userRepo.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword()))
            throw new RuntimeException("Invalid credentials");

        return new AuthResponse(jwtUtil.generateToken(user.getEmail()));
    }
}