package com.epam.demo.service;

import com.epam.demo.dto.AuthRequest;
import com.epam.demo.dto.AuthResponse;

public interface AuthService {
    AuthResponse login(AuthRequest request);
}
