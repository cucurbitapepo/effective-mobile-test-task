package com.example.bankcards.service;

import com.example.bankcards.dto.request.JwtRequestDto;
import com.example.bankcards.dto.response.JwtResponseDto;

public interface AuthService {
    JwtResponseDto createAuthToken(JwtRequestDto authRequest);
}
