package com.example.bankcards.service.impl;

import com.example.bankcards.dto.request.UserInfoRequestDto;
import com.example.bankcards.dto.response.AdminUserInfoResponseDto;
import com.example.bankcards.entity.User;
import com.example.bankcards.entity.enums.UserRole;
import com.example.bankcards.exception.UserNotFoundException;
import com.example.bankcards.mapper.UserMapperForAdmin;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.service.AdminUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {
    private final static String DELETED_SUCCESSFULLY = "Пользователь с ID: %s успешно удалён";

    private final UserRepository userRepository;
    private final UserMapperForAdmin mapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    private Optional<User> findById(String userId) {
        return userRepository.findById(UUID.fromString(userId));
    }

    @Override
    public AdminUserInfoResponseDto getUserInformationById(String userId) {
        return findById(userId)
                .map(mapper::entityToDto)
                .orElseThrow(UserNotFoundException::new);
    }

    @Override
    public List<AdminUserInfoResponseDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(mapper::entityToDto)
                .toList();
    }

    @Override
    public AdminUserInfoResponseDto createUser(UserInfoRequestDto userInfoRequestDto) {
        User user = mapper.dtoToEntity(userInfoRequestDto);
        String rawPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(rawPassword);
        User savedUser = userRepository.save(user);
        return mapper.entityToDto(savedUser);
    }

    @Transactional
    @Override
    public AdminUserInfoResponseDto updateUser(String userId, UserInfoRequestDto userInfoRequestDto) {
        return findById(userId)
                .map(user -> {
                    user.setFirstAndLastName(userInfoRequestDto.getFirstAndLastName());
                    user.setUsername(userInfoRequestDto.getUsername());
                    user.setPassword(userInfoRequestDto.getPassword());
                    user.setRole(UserRole.valueOf(userInfoRequestDto.getRole()));
                    return mapper.entityToDto(userRepository.save(user));
                })
                .orElseThrow(UserNotFoundException::new);
    }

    @Override
    public String deleteUser(String userId) {
        userRepository.deleteById(UUID.fromString(userId));
        return String.format(DELETED_SUCCESSFULLY, userId);
    }
}