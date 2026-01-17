package com.skillsync.service.impl;

import com.skillsync.dto.AuthRequest;
import com.skillsync.dto.AuthResponse;
import com.skillsync.dto.RegisterRequest;
import com.skillsync.dto.UserDto;
import com.skillsync.entity.Role;
import com.skillsync.entity.User;
import com.skillsync.mapper.UserMapper;
import com.skillsync.repository.UserRepository;
import com.skillsync.security.JwtService;
import com.skillsync.service.AuthService;
import com.skillsync.service.EmailService;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    private final UserMapper userMapper;

    public AuthServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           JwtService jwtService,
                           AuthenticationManager authenticationManager,
                           EmailService emailService,
                           UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.emailService = emailService;
        this.userMapper = userMapper;
    }

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email already registered");
        }
        Role role = Role.ROLE_USER; // default role for self-registration
        User user = User.builder()
            .email(request.email())
            .password(passwordEncoder.encode(request.password()))
            .role(role)
            .build();
        User saved = userRepository.save(user);
        emailService.sendWelcomeEmail(saved.getEmail(), saved.getEmail());
        UserDetails principal = org.springframework.security.core.userdetails.User.builder()
                .username(saved.getEmail())
                .password(saved.getPassword())
                .roles(saved.getRole().name().replace("ROLE_", ""))
                .build();
        String token = jwtService.generateToken(principal);
        return new AuthResponse(token, jwtService.extractClaim(token, claims -> claims.getExpiration().toInstant()),
            userMapper.toDto(saved));
    }

    @Override
    public AuthResponse login(AuthRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        UserDetails principal = org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole().name().replace("ROLE_", ""))
                .build();
        String token = jwtService.generateToken(principal);
        return new AuthResponse(token, jwtService.extractClaim(token, claims -> claims.getExpiration().toInstant()),
            userMapper.toDto(user));
    }

}
