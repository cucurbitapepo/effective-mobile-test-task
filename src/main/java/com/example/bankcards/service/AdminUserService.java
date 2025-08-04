package com.example.bankcards.service;

import com.example.bankcards.dto.request.UserInfoRequestDto;
import com.example.bankcards.dto.response.AdminUserInfoResponseDto;

import java.util.List;

public interface AdminUserService {
    AdminUserInfoResponseDto getUserInformationById(String userId);
    List<AdminUserInfoResponseDto> getAllUsers();
    AdminUserInfoResponseDto createUser(UserInfoRequestDto userInfoRequestDto);
    AdminUserInfoResponseDto updateUser(String userId, UserInfoRequestDto userInfoRequestDto);
    String deleteUser(String userId);
}
