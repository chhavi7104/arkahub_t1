package com.chhavi.smartbanking.service;

import com.chhavi.smartbanking.dto.request.LoginRequest;
import com.chhavi.smartbanking.dto.request.RegisterRequest;
import com.chhavi.smartbanking.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
}