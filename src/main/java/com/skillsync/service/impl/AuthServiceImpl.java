package com.skillsync.service.impl;

import com.skillsync.dto.AuthRequest;
import com.skillsync.dto.AuthResponse;
import com.skillsync.dto.RegisterRequest;
import com.skillsync.dto.TwoFactorLoginResponse;
import com.skillsync.dto.TwoFactorVerifyRequest;
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

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    private final UserMapper userMapper;
    private final SecureRandom secureRandom = new SecureRandom();
    private final Map<String, TempCode> twoFactorCache = new ConcurrentHashMap<>();

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
    public TwoFactorLoginResponse login(AuthRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        User user = userRepository.findByEmail(request.email())
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

        cleanupExpired();
        String code = generateCode();
        String token = UUID.randomUUID().toString();
        twoFactorCache.put(token, new TempCode(user.getEmail(), code, Instant.now().plusSeconds(600)));

        emailService.sendTwoFactorCode(user.getEmail(), code);
        return new TwoFactorLoginResponse(true, token, "2FA code sent", user.getEmail());
    }

    @Override
    public AuthResponse verifyTwoFactor(TwoFactorVerifyRequest request) {
        cleanupExpired();

        TempCode temp = twoFactorCache.get(request.twoFactorToken());
        if (temp == null || temp.expiresAt().isBefore(Instant.now())) {
            twoFactorCache.remove(request.twoFactorToken());
            throw new IllegalArgumentException("Invalid or expired token");
        }

        if (!temp.code().equals(request.code())) {
            throw new IllegalArgumentException("Invalid code");
        }

        twoFactorCache.remove(request.twoFactorToken());
        User user = userRepository.findByEmail(temp.email())
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

    private void cleanupExpired() {
        Instant now = Instant.now();
        twoFactorCache.entrySet().removeIf(e -> e.getValue().expiresAt().isBefore(now));
    }

    private String generateCode() {
        int number = secureRandom.nextInt(900_000) + 100_000; // 6-digit
        return String.valueOf(number);
    }

    private record TempCode(String email, String code, Instant expiresAt) { }

}
