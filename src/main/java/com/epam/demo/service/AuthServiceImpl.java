package com.epam.demo.service;

import com.epam.demo.dto.AuthRequest;
import com.epam.demo.dto.AuthResponse;
import com.epam.demo.entity.Role;
import com.epam.demo.entity.User;
import com.epam.demo.repository.UserRepository;
import com.epam.demo.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public AuthResponse login(AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByUsername(request.getUsername())
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setUsername(request.getUsername());
                    newUser.setPassword(passwordEncoder.encode(request.getPassword()));
                    newUser.setRoles(Set.of(Role.USER));
                    return userRepository.save(newUser);
                });

        String token = jwtService.generateToken(user.getUsername(), user.getRoles());

        return new AuthResponse(token);
    }
}
